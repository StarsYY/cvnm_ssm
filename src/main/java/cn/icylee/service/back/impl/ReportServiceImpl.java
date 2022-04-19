package cn.icylee.service.back.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.ArticleMapper;
import cn.icylee.dao.CommentMapper;
import cn.icylee.dao.ReportMapper;
import cn.icylee.dao.UserMapper;
import cn.icylee.service.back.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public TableParameter setIdsTool(TableParameter tableParameter) {
        if (tableParameter.getNickname() != null && !tableParameter.getNickname().equals("")) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNicknameLike("%" + tableParameter.getNickname() + "%");

            List<User> userList = userMapper.selectByExample(userExample);
            if (userList.size() > 0) {
                StringBuilder ids = new StringBuilder();
                for (User user : userList) {
                    ids.append(user.getUid()).append(",");
                }
                tableParameter.setIds(ids.toString());
            } else {
                tableParameter.setIds(",0,");
            }
        }
        return tableParameter;
    }

    @Override
    public int getReportTotal(TableParameter tableParameter) {
        return reportMapper.getReportTotal(tableParameter);
    }

    @Override
    public List<Report> getPageReport(TableParameter tableParameter) {
        List<Report> reportList = reportMapper.getReportList(tableParameter);

        for (Report report : reportList) {
            if (report.getDatasource().equals("article")) {
                report.setReReportContent(articleMapper.selectByPrimaryKey(report.getDataid()).getTitle());
            } else {
                report.setReReportContent(commentMapper.selectByPrimaryKey(report.getDataid()).getComment());
            }
        }
        return reportList;
    }

    @Override
    public int deleteReport(int id) {
        return reportMapper.deleteByPrimaryKey(id);
    }
}
