package co.yixiang.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.modules.activity.domain.YxStoreCouponIssue;
import co.yixiang.modules.activity.service.YxStoreCouponIssueService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueQueryCriteria;
import co.yixiang.utils.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

/**
* @author hupeng
* @date 2019-11-09
*/
@Api(tags = "发布管理")
@RestController
@RequestMapping("api")
public class YxStoreCouponIssueController {

    @Autowired
    private YxStoreCouponIssueService yxStoreCouponIssueService;

    @Log("查询已发布")
    @ApiOperation(value = "查询已发布")
    @GetMapping(value = "/yxStoreCouponIssue")
    @PreAuthorize("hasAnyRole('ADMIN','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_SELECT')")
    public ResponseEntity getYxStoreCouponIssues(YxStoreCouponIssueQueryCriteria criteria, Pageable pageable){
        criteria.setIsDel(0);
        return new ResponseEntity(yxStoreCouponIssueService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("发布")
    @ApiOperation(value = "发布")
    @PostMapping(value = "/yxStoreCouponIssue")
    @PreAuthorize("hasAnyRole('ADMIN','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreCouponIssue resources){
        if(ObjectUtil.isNotNull(resources.getStartTimeDate())){
            resources.setStartTime(OrderUtil.
                    dateToTimestamp(resources.getStartTimeDate()));
        }
        if(ObjectUtil.isNotNull(resources.getEndTimeDate())){
            resources.setEndTime(OrderUtil.
                    dateToTimestamp(resources.getEndTimeDate()));
        }
        if(resources.getTotalCount() > 0) {
            resources.setRemainCount(resources.getTotalCount());
        }
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(yxStoreCouponIssueService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改状态")
    @ApiOperation(value = "修改状态")
    @PutMapping(value = "/yxStoreCouponIssue")
    @PreAuthorize("hasAnyRole('ADMIN','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreCouponIssue resources){
        yxStoreCouponIssueService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yxStoreCouponIssue/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','YXSTORECOUPONISSUE_ALL','YXSTORECOUPONISSUE_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        YxStoreCouponIssue resources = new YxStoreCouponIssue();
        resources.setId(id);
        resources.setIsDel(1);
        yxStoreCouponIssueService.update(resources);
        return new ResponseEntity(HttpStatus.OK);
    }
}