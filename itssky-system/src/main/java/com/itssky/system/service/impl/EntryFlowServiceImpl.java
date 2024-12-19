package com.itssky.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itssky.system.domain.TbStateEntry;
import com.itssky.system.mapper.EntryFlowMapper;
import com.itssky.system.service.IEntryFlowService;
import org.springframework.stereotype.Service;

@Service
public class EntryFlowServiceImpl extends ServiceImpl<EntryFlowMapper, TbStateEntry> implements IEntryFlowService {

}
