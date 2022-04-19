package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.*;
import cn.icylee.service.front.GrowService;
import cn.icylee.service.front.PersonalService;
import cn.icylee.utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    PreferMapper preferMapper;

    @Autowired
    PlateMapper plateMapper;

    @Autowired
    VerifyMapper verifyMapper;

    @Autowired
    IntegralMapper integralMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    MedalMapper medalMapper;

    @Autowired
    GrowService growService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    DiscussMapper discussMapper;

    @Autowired
    ReportMapper reportMapper;

    private User getUserByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNicknameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0);
    }

    @Override
    public User getUser(String username, String loginName) {
        User user = getUserByUsername(username);
        user.setGrow(Tool.setLevel(user.getGrow()));

        FollowExample feFans = new FollowExample();
        feFans.createCriteria().andDataidEqualTo(user.getUid()).andDatasourceEqualTo("user");

        user.setFans(followMapper.countByExample(feFans));

        FollowExample feFollow = new FollowExample();
        feFollow.createCriteria().andUseridEqualTo(user.getUid()).andDatasourceEqualTo("user");
        user.setAttention(followMapper.countByExample(feFollow));

        FollowExample FE = new FollowExample();
        FE.createCriteria().andUseridEqualTo(getUserByUsername(loginName).getUid()).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
        user.setFollow(followMapper.countByExample(FE) != 0);

        user.setPassword("");

        return user;
    }

    @Override
    public List<Medal> getUserMedal(String username) {
        return medalMapper.getUserMedal(getUserByUsername(username).getUid());
    }

    @Override
    public List<Medal> getAllMedal() {
        return medalMapper.selectByExample(null);
    }

    @Override
    public int updateUserSummary(User user) {
        user.setUpdatetime(new Date());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Map<String, Integer> getCommunication(String username) {
        Map<String, Integer> communication = new HashMap<>();
        int id = getUserByUsername(username).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(id);
        communication.put("文章", articleMapper.countByExample(articleExample));

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andUseridEqualTo(id);
        communication.put("回复", commentMapper.countByExample(commentExample));

        communication.put("获赞", userMapper.getArticleUp(id) + userMapper.getCommentUp(id));

        return communication;
    }

    @Override
    public List<Article> getNewArticle(String username) {
        int id = getUserByUsername(username).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(id).andStatusEqualTo("已发布").andIsdelEqualTo(0);
        articleExample.setOrderByClause(" createtime desc LIMIT 10");

        List<Article> articleList = articleMapper.selectByExample(articleExample);
        for (Article article : articleList) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));
        }

        return articleList;
    }

    @Override
    public List<User> getFollow(String username) {
        List<User> userList = userMapper.getFollowUser(getUserByUsername(username).getUid());
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));
        }

        return userList;
    }

    @Override
    public List<User> getExpert(String username) {
        List<User> userList = userMapper.getExpert();
        if (userList.size() > 6) {
            Collections.shuffle(userList);
            userList = userList.subList(0, 6);
        }
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));

            if (!username.equals("")) {
                FollowExample isFollow = new FollowExample();
                isFollow.createCriteria().andUseridEqualTo(getUserByUsername(username).getUid()).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
                user.setFollow(followMapper.countByExample(isFollow) != 0);
            }
        }
        return userList;
    }

    @Override
    public List<User> getFans(String username) {
        List<User> userList = userMapper.getFansUser(getUserByUsername(username).getUid());
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));
        }

        return userList;
    }

    @Override
    public List<Article> getCollect(Index index) {
        int id = getUserByUsername(index.getUsername()).getUid();
        index.setUid(id);

        List<Article> articleList = articleMapper.getCollectArticle(index);
        for (Article article : articleList) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));

            FollowExample feCollect = new FollowExample();
            feCollect.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setCollect(followMapper.countByExample(feCollect));

            PreferExample peLike = new PreferExample();
            peLike.createCriteria().andUseridEqualTo(id).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

            List<Prefer> preferList = preferMapper.selectByExample(peLike);
            if (preferList.size() == 0) {
                article.setLike(false);
                article.setUnlike(false);
            } else {
                if (preferList.get(0).getPush() == 1) {
                    article.setLike(true);
                    article.setUnlike(false);
                } else if (preferList.get(0).getPush() == 0) {
                    article.setLike(false);
                    article.setUnlike(true);
                }
            }

            FollowExample fe = new FollowExample();
            FollowExample.Criteria fc = fe.createCriteria();
            fc.andUseridEqualTo(id).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setStar(followMapper.selectByExample(fe).size() != 0);
        }

        return articleList;
    }

    @Override
    public List<Message> getMySystemMessage(Index index) {
        int uid = getUserByUsername(index.getUsername()).getUid();

        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andReceiveuidEqualTo(uid).andTypeEqualTo(0);
        messageExample.setOrderByClause(" createtime desc limit " + index.getPage() + "," + index.getLimit());
        List<Message> messageList = messageMapper.selectByExample(messageExample);

        for (Message message : messageList) {
            message.setSendername(userMapper.selectByPrimaryKey(message.getSenderuid()).getNickname());

            switch (message.getDatasource()) {
                case "article":
                    message.setArticleTitle(articleMapper.selectByPrimaryKey(message.getAddition()).getTitle());
                    message.setReceiveContent(commentMapper.selectByPrimaryKey(Integer.parseInt(message.getContent())).getComment());
                    break;
                case "comment":
                    Comment comment = commentMapper.selectByPrimaryKey(message.getAddition());
                    message.setReceiveContent(commentMapper.selectByPrimaryKey(Integer.parseInt(message.getContent())).getComment());
                    message.setReceivedContent(comment.getComment());
                    message.setReceiveTime(comment.getCreatetime());
                    break;
                case "course":
                    message.setCourseName(courseMapper.selectByPrimaryKey(message.getAddition()).getName());
                    message.setReceiveContent(discussMapper.selectByPrimaryKey(Integer.parseInt(message.getContent())).getDiscuss());
                    break;
                case "discuss":
                    Discuss discuss = discussMapper.selectByPrimaryKey(message.getAddition());
                    message.setCourseName(courseMapper.selectByPrimaryKey(discuss.getCourseid()).getName());
                    message.setReceiveContent(discussMapper.selectByPrimaryKey(Integer.parseInt(message.getContent())).getDiscuss());
                    message.setReceivedContent(discuss.getDiscuss());
                    message.setReceiveTime(discuss.getCreatetime());
                    break;
            }
        }
        return messageList;
    }

    @Override
    public List<Message> getMyAdministratorMessage(Index index) {
        int uid = getUserByUsername(index.getUsername()).getUid();

        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andReceiveuidEqualTo(uid).andTypeEqualTo(1);
        messageExample.setOrderByClause(" createtime desc limit " + index.getPage() + "," + index.getLimit());
        return messageMapper.selectByExample(messageExample);
    }

    @Override
    public int updateMessage(Message message) {
        message.setRead(1);
        return messageMapper.updateByPrimaryKeySelective(message);
    }

    @Override
    public int updateMessageAll(Message message) {
        int uid = getUserByUsername(message.getUsername()).getUid();

        MessageExample messageExample = new MessageExample();
        messageExample.createCriteria().andReceiveuidEqualTo(uid).andTypeEqualTo(message.getType());
        List<Message> messageList = messageMapper.selectByExample(messageExample);

        for (Message mm : messageList) {
            if (mm.getRead() == 0) {
                mm.setRead(1);

                if (messageMapper.updateByPrimaryKeySelective(mm) == 0) {
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int deleteSelectMessage(int[] ids) {
        for (int i : ids) {
            if (messageMapper.deleteByPrimaryKey(i) == 0) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public List<Article> getMyArticle(Index index) {
        int id = getUserByUsername(index.getUsername()).getUid();

        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andUseridEqualTo(id).andStatusEqualTo("已发布").andIsdelEqualTo(0);
        articleExample.setOrderByClause(" createtime ASC LIMIT " + index.getPage() + "," + index.getLimit());
        List<Article> articleList = articleMapper.selectByExample(articleExample);

        for (Article article : articleList) {
            article.setPlate(plateMapper.selectByPrimaryKey(article.getPlateid()).getPlate());

            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andArticleidEqualTo(article.getId());
            article.setComment(commentMapper.countByExample(commentExample));

            PreferExample peUp = new PreferExample();
            peUp.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(1);
            article.setUp(preferMapper.countByExample(peUp));

            PreferExample peDown = new PreferExample();
            peDown.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId()).andPushEqualTo(0);
            article.setDown(preferMapper.countByExample(peDown));

            FollowExample feCollect = new FollowExample();
            feCollect.createCriteria().andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setCollect(followMapper.countByExample(feCollect));

            PreferExample peLike = new PreferExample();
            peLike.createCriteria().andUseridEqualTo(getUserByUsername(index.getLoginName()).getUid()).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());

            List<Prefer> preferList = preferMapper.selectByExample(peLike);
            if (preferList.size() == 0) {
                article.setLike(false);
                article.setUnlike(false);
            } else {
                if (preferList.get(0).getPush() == 1) {
                    article.setLike(true);
                    article.setUnlike(false);
                } else if (preferList.get(0).getPush() == 0) {
                    article.setLike(false);
                    article.setUnlike(true);
                }
            }

            FollowExample fe = new FollowExample();
            FollowExample.Criteria fc = fe.createCriteria();
            fc.andUseridEqualTo(getUserByUsername(index.getLoginName()).getUid()).andDatasourceEqualTo("article").andDataidEqualTo(article.getId());
            article.setStar(followMapper.selectByExample(fe).size() != 0);
        }

        return articleList;
    }

    @Override
    public int deleteMyArticle(int id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        article.setIsdel(1);
        article.setUpdatetime(new Date());

        if (articleMapper.updateByPrimaryKeySelective(article) > 0) {
            return growService.updateDecreaseIntegralAndGrowFromArticleOrCourse(article.getUserid());
        }
        return 0;
    }

    private List<User> getMyFansOrFollow(boolean is, Index index) {
        index.setUid(getUserByUsername(index.getUsername()).getUid());

        List<User> userList;
        if (is) {
            userList = userMapper.getFansUser2(index);
        } else {
            userList = userMapper.getFollowUser2(index);
        }
        for (User user : userList) {
            user.setGrow(Tool.setLevel(user.getGrow()));
            user.setPassword("");

            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andUseridEqualTo(user.getUid());
            user.setArticle(articleMapper.countByExample(articleExample));

            FollowExample feFollow =  new FollowExample();
            feFollow.createCriteria().andUseridEqualTo(user.getUid()).andDatasourceEqualTo("user");
            user.setAttention(followMapper.countByExample(feFollow));

            FollowExample feFans = new FollowExample();
            feFans.createCriteria().andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
            user.setFans(followMapper.countByExample(feFans));

            FollowExample isFollow = new FollowExample();
            isFollow.createCriteria().andUseridEqualTo(getUserByUsername(index.getLoginName()).getUid()).andDatasourceEqualTo("user").andDataidEqualTo(user.getUid());
            user.setFollow(followMapper.countByExample(isFollow) != 0);
        }
        return userList;
    }

    @Override
    public List<User> getMyFans(Index index) {
        return getMyFansOrFollow(true, index);
    }

    @Override
    public List<User> getMyFollow(Index index) {
        return getMyFansOrFollow(false, index);
    }

    @Override
    public List<Plate> getMyFollowPlate(Index index) throws ParseException {
        int uid = getUserByUsername(index.getUsername()).getUid();
        index.setUid(uid);

        List<Plate> plateList = plateMapper.getFollowPlate(index);
        for (Plate plate : plateList) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH )+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String START = year + "-" + month + "-" + day + " 00:00:00";
            String FINAL = year + "-" + month + "-" + day + " 23:59:59";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date dateStart = format.parse(START);
            Date dateFinal = format.parse(FINAL);

            ArticleExample articleExample = new ArticleExample();
            ArticleExample.Criteria criteria = articleExample.createCriteria();
            criteria.andPlateidEqualTo(plate.getId()).andStatusEqualTo("已发布").andIsdelEqualTo(0);
            List<Article> articleList = articleMapper.selectByExample(articleExample);

            int commentCount = 0;
            for (Article article : articleList) {
                CommentExample commentExample = new CommentExample();
                commentExample.createCriteria().andArticleidEqualTo(article.getId()).andStatusEqualTo(1);
                commentCount += commentMapper.countByExample(commentExample);
            }
            plate.setReplyCount(commentCount);

            criteria.andCreatetimeBetween(dateStart, dateFinal);
            plate.setTodayArticle(articleMapper.countByExample(articleExample));

            FollowExample followExample = new FollowExample();
            followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("plate").andDataidEqualTo(plate.getId());
            plate.setFollow(followMapper.countByExample(followExample) != 0);
        }
        return plateList;
    }

    @Override
    public List<Label> getMyFollowLabel(Index index) throws ParseException {
        int uid = getUserByUsername(index.getUsername()).getUid();
        index.setUid(uid);

        List<Label> labelList = labelMapper.getFollowLabel(index);
        for (Label label : labelList) {
            ArticleExample articleExample = new ArticleExample();
            ArticleExample.Criteria criteria = articleExample.createCriteria();
            criteria.andLabelidLike("%," + label.getId() + ",%").andStatusEqualTo("已发布").andIsdelEqualTo(0);
            label.setArticleCount(articleMapper.countByExample(articleExample));

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH ) + 1;
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            String START = year + "-" + month + "-" + 1 + " 00:00:00";
            String FINAL = year + "-" + month + "-" + lastDay + " 23:59:59";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date dateStart = format.parse(START);
            Date dateFinal = format.parse(FINAL);

            criteria.andCreatetimeBetween(dateStart, dateFinal);
            label.setMonthArticleCount(articleMapper.countByExample(articleExample));

            FollowExample followExample = new FollowExample();
            followExample.createCriteria().andUseridEqualTo(uid).andDatasourceEqualTo("label").andDataidEqualTo(label.getId());
            label.setFollow(followMapper.countByExample(followExample) != 0);
        }
        return labelList;
    }

    @Override
    public Verify getVerify(String username) {
        int id = getUserByUsername(username).getUid();
        VerifyExample verifyExample = new VerifyExample();
        verifyExample.createCriteria().andUseridEqualTo(id).andPositionEqualTo("专家").andStatusEqualTo(1);
        List<Verify> verifyList = verifyMapper.selectByExample(verifyExample);
        return verifyList.size() > 0 ? verifyList.get(0) : null;
    }

    @Override
    public List<Integral> getIntegral(String username, Integer page) {
        int id = getUserByUsername(username).getUid();
        IntegralExample integralExample = new IntegralExample();
        integralExample.createCriteria().andUseridEqualTo(id);
        page = (page - 1) * 10;
        integralExample.setOrderByClause(" createtime desc limit " + page + ", 10");
        return integralMapper.selectByExample(integralExample);
    }

    @Override
    public Integer getIntegralTotal(String username) {
        int id = getUserByUsername(username).getUid();
        IntegralExample integralExample = new IntegralExample();
        integralExample.createCriteria().andUseridEqualTo(id);
        return integralMapper.countByExample(integralExample);
    }

    @Override
    public List<Article> getMyDraft(Index index) {
        index.setUid(getUserByUsername(index.getUsername()).getUid());
        return articleMapper.getMyDraft(index);
    }

    @Override
    public int deleteMyDraft(int id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Article> getMyAudit(Index index) {
        index.setUid(getUserByUsername(index.getUsername()).getUid());
        return articleMapper.getMyAudit(index);
    }

    @Override
    public int deleteMyAudit(int id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Comment> getMyComment(Index index) {
        index.setUid(getUserByUsername(index.getUsername()).getUid());
        List<Comment> commentList = commentMapper.getMyReply(index);

        for (Comment comment : commentList) {
            if (comment.getComid() != 0) {
                comment.setCm(commentMapper.getMyReplyRe(comment.getComid()));
            } else {
                comment.setCm(null);
            }
        }
        return commentList;
    }

    private List<Integer> getDelId(int id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andComidEqualTo(id);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        List<Integer> list = new ArrayList<>();
        list.add(id);
        for (Comment comment : commentList) {
            list.add(comment.getId());
            getDelId(comment.getId());
        }
        return list;
    }

    @Override
    public int[] deleteMyComment(int id) {
        int[] ids = getDelId(id).stream().mapToInt(Integer::valueOf).toArray();
        StringBuilder s = new StringBuilder();
        for (int i : ids) {
            s.append(i).append(",");
        }

        String ss = s.substring(0, s.length() - 1);
        int uid = commentMapper.selectByPrimaryKey(id).getUserid();
        if (commentMapper.deleteComment(ss) >= 0) {
            preferMapper.deletePreferByComment(ss);
            reportMapper.deleteReportByComment(ss);
            messageMapper.deleteMessageByComment(ss);

            MessageExample messageExample = new MessageExample();
            messageExample.createCriteria().andContentEqualTo(String.valueOf(id)).andDatasourceEqualTo("article");
            messageMapper.deleteByExample(messageExample);

            if (growService.updateDecreaseIntegralAndGrowFromCommentOrDiscuss(uid) > 0) {
                return ids;
            }
        }
        return null;
    }

}
