package cn.icylee.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lenovo
 */
@Data
public class ChunkFileDto {
    /**
     * 当前块的次序，第一个块是 1，注意不是从 0 开始的
     */
    private int chunkNumber;
    /**
     * 文件被分成块的总数
     */
    private int totalChunks;
    /**
     * 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大。
     */
    private long chunkSize;
    /**
     * 当前块的大小，实际大小
     */
    private long currentChunkSize;
    /**
     * 文件总大小
     */
    private int totalSize;
    /**
     * 这个就是每个文件的唯一标示(md5值)
     */
    private String identifier;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 文件夹上传的时候文件的相对路径属性。
     */
    private String relativePath;
    /**
     * 文件内容
     */
    private MultipartFile file;
}
