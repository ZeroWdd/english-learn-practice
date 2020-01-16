package com.learn.web.service;

import com.learn.web.mapper.AdminMapper;
import com.learn.web.pojo.Admin;
import com.learn.web.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:27
 * @Description:
 */
@Service
public class AdminService implements UserDetailsService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return adminMapper.findByName(s);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public PageBean<Admin> queryPage(Map<String, Object> paramMap) {
        PageBean<Admin> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Admin> datas = adminMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = adminMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public Admin selectByName(String username) {
        return adminMapper.selectByName(username);
    }

    public Admin selectByEmail(String email) {
        return adminMapper.selectByEmail(email);
    }

    public int editByAdmin(Admin admin) {
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    public int insertAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }

    public int delByAdminIds(List<Integer> ids) {
        int count = 0;
        for(Integer id : ids){
            adminMapper.deleteByPrimaryKey(id);
            count++;
        }
        return count;
    }
}
