package com.itssky.web.base;

import com.itssky.common.core.domain.AjaxResult;
import com.itssky.common.core.domain.model.LoginUser;
import com.itssky.system.service.TbStationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 收费信息 controller
 *
 * @author myc
 * @version 1.0.0
 * @date 2024/11/27 9:39
 */
@RestController
@RequestMapping("tbstation")
public class TbStationInfoController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TbStationInfoController.class);

    @Autowired
    private TbStationInfoService tbStationInfoService;

    /**
     *自动计算当前
     *
     * @return {@link AjaxResult }
     */
    @GetMapping("listStationSelect")
    public AjaxResult listStationSelect() {
        HashMap<String, Object> tempMap = new HashMap<>();
        List<Map<String, Object>> tempList = tbStationInfoService.listStationSelect();
        Integer stationId = tbStationInfoService.currentAssignStationId();
        tempMap.put("array", tempList);
        tempMap.put("defaultValue", stationId);
        return AjaxResult.success(tempMap);
    }

    /**
     * <p>前端下拉框站选择</p>
     *
     * @return {@link AjaxResult }
     */
    @GetMapping("stationSelectList")
    public AjaxResult stationSelectList() {
        AjaxResult ajaxResult = AjaxResult.success();
        Map<String, Object> tempMap = tbStationInfoService.stationSelectList();
        ajaxResult.put("data", Collections.singletonList(tempMap));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        Integer level = loginUser.getLevel();
        List<List<Integer>> tempList = handleCheckList(tempMap, level);
        ajaxResult.put("selectData", tempList);
        return ajaxResult;
    }

    /**
     * 计算当前应当选中的收费站
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;小伟 </p>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;/ &nbsp;&nbsp;&nbsp;&nbsp;   \</p>
     * <p>&nbsp;&nbsp;小飞飞&nbsp;&nbsp; 小桂</p>
     * <p>
     * 层序遍历顺序即 小伟->小飞飞->小桂
     * </p>
     *
     * @param stationSelect 车站选择
     * @param level         层级
     * @return {@link List }<{@link List }<{@link Integer }>>
     */
    private static List<List<Integer>> handleCheckList(Map<String, Object> stationSelect, Integer level) {
        List<List<Integer>> resultList = new ArrayList<>();
        //假设此处queue为小飞飞，小飞飞是一个能不断吃屎的物种，并且能不断拉出来
        Queue<Map<String, Object>> queue = new LinkedList<>();
        queue.add(stationSelect);
        //判断小飞飞是否还能拉出来
        while (!queue.isEmpty()) {
            Map<String, Object> poll = queue.poll();
            Integer value = (Integer)poll.get("value");
            if (value != null && value != -1) {
                //属于站，添加 自己猜
                resultList.add(level == 1 ? Arrays.asList(-1, -1, value)
                    : level == 2 ? Arrays.asList(-1, value) : Collections.singletonList(value));
            } else if (value != null && -1 == value) {
                //看是否存在children
                List<Map<String, Object>> children = (List<Map<String, Object>>)poll.get("children");
                if (CollectionUtils.isEmpty(children)) {
                    continue;
                }
                //喂小飞飞吃屎
                for (Map<String, Object> child : children) {
                    queue.add(child);
                }
            }
        }
        return resultList;
    }
}
