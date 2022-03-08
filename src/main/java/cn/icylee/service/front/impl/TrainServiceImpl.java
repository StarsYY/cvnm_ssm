package cn.icylee.service.front.impl;

import cn.icylee.bean.*;
import cn.icylee.dao.CourseMapper;
import cn.icylee.dao.ModularMapper;
import cn.icylee.service.front.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    ModularMapper modularMapper;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public List<Modular> getBusinessArea() {
        ModularExample modularExample = new ModularExample();
        modularExample.createCriteria().andAncestorEqualTo(0);
        return modularMapper.selectByExample(modularExample);
    }

    @Override
    public List<Modular> getBusinessModule(int ancestor) {
        if (ancestor != 0) {
            ModularExample modularExample = new ModularExample();
            modularExample.createCriteria().andAncestorEqualTo(ancestor);
            return modularMapper.selectByExample(modularExample);
        }
        return null;
    }

    @Override
    public List<Course> getCourseList(Modular modular) {
        if (modular.getId() != 0) {
            modular.setIds(modular.getId().toString());
        } else {
            if (modular.getAncestor() != 0) {
                ModularExample modularExample = new ModularExample();
                modularExample.createCriteria().andAncestorEqualTo(modular.getAncestor());

                StringBuilder ids = new StringBuilder();
                for (Modular M : modularMapper.selectByExample(modularExample)) {
                    ids.append(M.getId()).append(",");
                }

                modular.setIds(ids.toString());
            }
        }
        return courseMapper.getAllCourseList(modular);
    }

    @Override
    public int getAllCourseTotal(Modular modular) {
        return courseMapper.getAllCourseTotal(modular);
    }

}
