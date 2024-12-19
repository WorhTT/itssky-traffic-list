package com.itssky.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbcorpinfo")
public class TbCorpInfo implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "corpno")
    private String corpno;

    @TableField(value = "corpname")
    private String corpname;

    @TableField(value = "corpshort")
    private String corpshort;

    @TableField(value = "corpid")
    private Integer corpid;

    @TableField(value = "ownerid")
    private Integer ownerid;

    @TableField(value = "ownername")
    private String ownername;

    @TableField(value = "`level`")
    private Integer level;

    @TableField(value = "indexno")
    private Integer indexno;

    @TableField(value = "spare1")
    private Integer spare1;

    @TableField(value = "spare2")
    private String spare2;

    private static final long serialVersionUID = 1L;
}