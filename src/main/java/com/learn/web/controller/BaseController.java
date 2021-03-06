package com.learn.web.controller;

import com.learn.web.pojo.*;
import com.learn.web.service.*;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.Const;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2020/1/12 15:53
 * @Description:
 */
@Controller
public class BaseController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private IconService iconService;
    @Autowired
    private TreeMenuService treeMenuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private WordService wordService;
    @Autowired
    private ReadService readService;
    @Autowired
    private SubjcetService subjcetService;
    @Autowired
    private PaperService paperService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/user/login")
    public String login(){
        return "user/login";
    }

    @RequestMapping("/user/reg")
    public String reg(){
        return "user/reg";
    }

    @RequestMapping("/learn")
    public String learn(Model model){
        int countCetFour = wordService.selectNumByCetFour();
        int countCetSix = wordService.selectNumByCetSix();
        int countNetem = wordService.selectNumByNetem();
        model.addAttribute("countCetFour",countCetFour);
        model.addAttribute("countCetSix",countCetSix);
        model.addAttribute("countNetem",countNetem);
        return "learn/index";
    }

    @RequestMapping("/exam")
    public String exam(Model model){

        return "exam/index";
    }

    /**
     * 跳转管理员登录界面
     * @return
     */
    @GetMapping("/manager/login")
    public String adminLogin(){
        return "manager/login";
    }

    /**
     * 管理员登出
     * @param session
     * @return
     */
    @GetMapping("/manager/logout")
    public String adminLogout(HttpSession session){
        session.invalidate();
        return "manager/login";
    }

    /**
     * 跳转管理员修改密码页面
     * @return
     */
    @GetMapping("/manager/password")
    public String adminPassword(){
        return "manager/common/password";
    }

    /**
     * 跳转管理员页面
     * @return
     */
    @GetMapping("/manager/admin")
    public String admin(){
        return "manager/admin/adminList";
    }

    /**
     * 跳转添加管理员页面
     * @return
     */
    @GetMapping("/manager/addAdmin")
    public String addAdmin(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Admin admin = adminService.selectById(id);
            model.addAttribute(Const.ADMIN,admin);
        }
        return "manager/admin/addAdmin";
    }

    @GetMapping("/manager/error/error403")
    public String error403(){
        return "/manager/error/error403";
    }

    /**
     * 日志页面
     * @return
     */
    @GetMapping("/manager/log")
    public String log(){
        return "manager/log/logList";
    }

    /**
     * 跳转后台页面
     * @return
     */
    @GetMapping("/manager/index")
    public String managerIndex(){
        return "manager/index";
    }

    /**
     * 异步加载后台主页
     * @return
     */
    @GetMapping("/manager/console")
    public String console(){
        return "manager/console";
    }

    /**
     * 跳转权限界面
     * @return
     */
    @GetMapping("/manager/permission")
    public String permission(){
        return "manager/permission/permissionList";
    }

    /**
     * 跳转添加权限页面
     * @param type
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/manager/addPermission")
    public String addPermission(String type, Integer id, Model model,HttpSession session){
        //加载icon列表,存入session
        if(session.getAttribute(Const.ICON) == null){
            List<Icon> iconList = iconService.selectAll();
            session.setAttribute(Const.ICON,iconList);
        }
        if(type != null && type.equals("edit")){
            TreeMenu treeMenu = treeMenuService.selectById(id);
            model.addAttribute(Const.TREEMENU,treeMenu);
        }else if(type != null && type.equals("add")){
            model.addAttribute("pid",id); //设为父id
        }else if(type != null && type.equals("addParent")){
            return "manager/permission/addParentNode";
        }
        return "manager/permission/addPermission";
    }

    @GetMapping("/manager/role")
    public String role(){
        return "manager/role/roleList";
    }

    /**
     * 跳转添加角色页面
     * @param type
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/manager/addRole")
    public String addRole(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Role role = roleService.selectById(id);
            model.addAttribute(Const.ROLE,role);
        }
        return "manager/role/addRole";
    }

    @GetMapping("/manager/allotPer")
    public String allotPer(Integer id,Model model){
        model.addAttribute("id",id);
        return "/manager/role/allotPer";
    }

    @GetMapping("/manager/word")
    public String word(){
        return "manager/word/wordList";
    }

    @GetMapping("/manager/addWord")
    public String addWord(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Word word = wordService.selectById(id);
            model.addAttribute(Const.WORD,word);
        }
        return "manager/word/addWord";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.removeAttribute(Const.USER);
        return "index";
    }

    @GetMapping("/learn/word/{type}")
    public String learnWord(Model model,@PathVariable String type,HttpSession session){
        User user = (User) session.getAttribute(Const.USER);
        if(user == null){
            return "/user/login";
        }
        List<Word> words = wordService.selectSome(type);
        if(words == null){
            return "/learn/index";
        }
        model.addAttribute(Const.WORD_LIST,words);
        model.addAttribute(Const.WORD_TYPE,type);
        return "/learn/word";
    }

    @GetMapping("/learn/read/one/{id}")
    public String read(@PathVariable String id,Model model,HttpSession session){
        User user = (User) session.getAttribute(Const.USER);
        if(user == null){
            return "/user/login";
        }
        Read read = readService.selectOne(id);
        model.addAttribute(Const.READ,read);
        return "learn/read";
    }

    @GetMapping("/exam/paper/one/{id}")
    public String paper(@PathVariable String id,Model model,HttpSession session){
//        User user = (User) session.getAttribute(Const.USER);
//        if(user == null){
//            return "/user/login";
//        }
        List<Subject> subjects = paperService.selectPaperSubject(id);
        // 分出单选和多选
        List<Subject> danxuan = subjects.stream().filter(subject -> subject.getAnswer().length() == 1).collect(Collectors.toList());
        List<Subject> duoxuan = subjects.stream().filter(subject -> subject.getAnswer().length() != 1).collect(Collectors.toList());
//        if(ArrayUtils.isNotEmpty(danxuan.toArray())){
//            model.addAttribute("danxuan",danxuan);
//        }
//        if(ArrayUtils.isNotEmpty(duoxuan.toArray())){
//            model.addAttribute("duoxuan",duoxuan);
//        }
        model.addAttribute("danxuan",danxuan);
        return "exam/paper";
    }

    @GetMapping("/manager/read")
    public String read(){
        return "manager/read/readList";
    }

    @GetMapping("/manager/subject")
    public String subject(){
        return "manager/subject/subjectList";
    }

    @GetMapping("/manager/paper")
    public String paper(){
        return "manager/paper/paperList";
    }

    /**
     * 跳转添加短文页面
     * @param type
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/manager/addRead")
    public String addRead(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Read read = readService.selectById(id);
            model.addAttribute(Const.READ,read);
        }
        return "manager/read/addRead";
    }

    /**
     * 跳转添加题目页面
     * @param type
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/manager/addSubject")
    public String addSubject(String type, Integer id, Model model){
        if(type != null && type.equals("edit")){
            Subject subject = subjcetService.selectById(id);
            model.addAttribute(Const.SUBJECT,subject);
        }
        return "manager/subject/addSubject";
    }
}
