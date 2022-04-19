package cn.icylee.controller.back;

import cn.icylee.bean.ChunkFileDto;
import cn.icylee.utils.ResponseData;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * @author cobra
 */
@RestController
@RequestMapping("/adm/course")
public class ChunkFileUploadController {

    /**
     * 分片存储的临死目录
     */
    private static final String TEMP_DIR = "F:\\file\\files_temp\\";

    /**
     * 文件最终存储的目录
     */
    private static final String SAVE_DIR = "E:\\IDEA\\ideaWeb\\upload\\video";

    /**
     * 分片名称的分隔符
     */
    private static final String NAME_SEPARATOR = "__";

    /**
     * 检测断点和md5
     */
    @GetMapping("upload/video")
    public Map<String, Object> checkFileMd5(ChunkFileDto chunkFileDto) throws IOException {
        String md5 = "72f24200bb28727fd8c985f1f08729e3";
//        log.info("校验文件的md5值是否存在");
        Map<String, Object> map = new HashMap<>();
        // 检测文件是否存在，遍历数据库，对照MD5
        // 有问题，不能妙传
        if (md5.equalsIgnoreCase(chunkFileDto.getIdentifier())) {
//            log.info("文件md5值:{},已存在实现秒传", chunkFileDto.getIdentifier());
            map.put("isExist", Boolean.TRUE);
            return ResponseData.success(map, "/upload");
        }
        //已上传的分片
        List<Integer> chunkExists = new LinkedList<>();
        String tempPath = TEMP_DIR + chunkFileDto.getIdentifier();
        try {
            if (Files.exists(Paths.get(TEMP_DIR + chunkFileDto.getIdentifier()))) {
                Files.list(Paths.get(tempPath)).forEach(path -> {
                    int index = path.getFileName().toString().lastIndexOf(NAME_SEPARATOR) + 2;
                    chunkExists.add(Integer.parseInt(path.getFileName().toString().substring(index)));
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果全部分片已上传则直接合并文件
        if (chunkExists.size() == chunkFileDto.getTotalChunks()) {
            map.put("isExist", Boolean.TRUE);
            // 执行文件的合并操作
            mergeChunkFile(chunkFileDto);
            return ResponseData.success(map, "/upload2");
        }
        //否则返回已上传的分片
        map.put("uploaded", getUploadedChunkNumber(chunkExists));
        //否则返回已上传的分片
        return ResponseData.success(map, "/upload3");
    }

    /**
     * 上传分片
     *
     * @param chunkFileDto
     */
    @PostMapping("upload/video")
    @ResponseBody
    public Map<String, Object> uploadChunkFile(ChunkFileDto chunkFileDto) throws IOException {

//        log.info("当前文件的ID:{},当前文件的大小:{},文件分片总数:{},当前分片数:{},件夹上传的时候文件的相对路径{}",
//                chunkFileDto.getIdentifier(), chunkFileDto.getTotalSize(),
//                chunkFileDto.getTotalChunks(), chunkFileDto.getChunkNumber(), chunkFileDto.getRelativePath());

        Map<String, Object> map = new HashMap<>();
        //如果只有一个分片则不进行合并操作，并且告知前端不需要调用合并接口
        if (chunkFileDto.getTotalChunks() == 1) {
            if (!Files.exists(Paths.get(SAVE_DIR))) {
                Files.createDirectories(Paths.get(SAVE_DIR));
            }
            Files.write(Paths.get(SAVE_DIR + File.separator + chunkFileDto.getFilename()), chunkFileDto.getFile().getBytes());
            map.put("merge", Boolean.FALSE);
            return ResponseData.success(map, "/postUpload");
        }

        //如果不止一个分片则需要存储分片文件，并告知前端需要在分片上传完成时调用合并接口
        String path = TEMP_DIR + chunkFileDto.getIdentifier();
        if (!Files.exists(Paths.get(path))) {
            Files.createDirectories(Paths.get(path));
        }
        Files.write(Paths.get(path + File.separator + chunkFileDto.getIdentifier() + NAME_SEPARATOR + chunkFileDto.getChunkNumber()), chunkFileDto.getFile().getBytes());
        map.put("merge", Boolean.TRUE);
        return ResponseData.success(map, "/postUpload2");
    }


    /**
     * 合并文件
     *
     * @return
     */
    @RequestMapping(value = "upload/video/merge", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> mergeChunkFile(@RequestBody ChunkFileDto chunkFileDto) throws IOException {
        //如果触发分片合并请求，后端未找到分片则给出错误提示
        String tempPath = TEMP_DIR + File.separator + chunkFileDto.getIdentifier();
        if (Files.notExists(Paths.get(tempPath))) {
            return ResponseData.error("chunk file does not exist");
        }
        if (!Files.exists(Paths.get(SAVE_DIR))) {
            Files.createDirectories(Paths.get(SAVE_DIR));
        }
        String targetFilePath = SAVE_DIR + File.separator + chunkFileDto.getFilename();
        //1.判断文件是否存在如果文件不存在则进行文件的合并操作
        if (!Files.exists(Paths.get(targetFilePath))) {
            Files.createFile(Paths.get(targetFilePath));
            Files.list(Paths.get(tempPath)).sorted((chunk1, chunk2) -> {
                String chunk1Name = chunk1.getFileName().toString();
                String chunk2Name = chunk2.getFileName().toString();
                int chunk1NumberIndex = chunk1Name.lastIndexOf(NAME_SEPARATOR) + 2;
                int chunk2NumberIndex = chunk2Name.lastIndexOf(NAME_SEPARATOR) + 2;
                return Integer.valueOf(chunk1Name.substring(chunk1NumberIndex)).compareTo(Integer.valueOf(chunk2Name.substring(chunk2NumberIndex)));
            }).forEach(path -> {
                try {
//                  log.info("当前合并分片number:{},分片总数:{}", path.getFileName(), chunkFileDto.getTotalChunks());
                    //以追加的形式写入文件
                    Files.write(Paths.get(targetFilePath), Files.readAllBytes(path), StandardOpenOption.APPEND);
                    //Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        // 删除块文件目录
//        Files.delete(Paths.get(tempPath));
        delFolder(tempPath);
        return ResponseData.success(chunkFileDto, "kong");
    }

    /**
     * 获取已经上传的分片数，因为前段是并发上传 可能存在上传的分片是1,2,3,5。此时返回给前段已上传的分片是1,2,3从第四个开始重传。
     *
     * @param chunkNumbers
     * @return
     */
    private static List<Integer> getUploadedChunkNumber(List<Integer> chunkNumbers) {
        List<Integer> result = new ArrayList<>();
        BitSet bitSet = new BitSet();
        for (int chunkNumber : chunkNumbers) {
            bitSet.set(chunkNumber);
        }
        int chunkNumberIndex = bitSet.nextClearBit(1);
        for (int i = 1; i < chunkNumberIndex; i++) {
            result.add(i);
        }
        return result;
    }

    @RequestMapping("upload/video/merge/ping")
    public String ping() {
        return "pong";
    }

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下的所有文件
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (String s : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + s);
            } else {
                temp = new File(path + File.separator + s);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + s);//先删除文件夹里面的文件
                delFolder(path + "/" + s);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }


}
