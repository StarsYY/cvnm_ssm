package cn.icylee.service.back;

import cn.icylee.bean.Video;
import cn.icylee.bean.TableParameter;

import java.util.List;

public interface VideoService {

    boolean saveVideo(List<Video> videoList, int id);

    int getVideoByCourseId(int id);

    TableParameter setIdsTool(TableParameter tableParameter);

    int getVideoTotal(TableParameter tableParameter);

    List<Video> getPageVideo(TableParameter tableParameter);

    int deleteVideo(int id);

}
