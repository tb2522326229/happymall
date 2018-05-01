package com.mall.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.commom.ServerResponse;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.IShippingService;

@Service("shippingService")
public class ShippingServiceImpl implements IShippingService {
	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ServerResponse add(int userId, Shipping shipping) {
		shipping.setUserId(userId);
		int resultCount = shippingMapper.insert(shipping);
		if(resultCount > 0){
			Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
		}
		return ServerResponse.createByErrorMessage("新建地址失败");
	}

	@Override
	public ServerResponse del(Integer userId, Integer shippingId) {
		int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
		if(resultCount > 0){
			return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
	}

	@Override
	public ServerResponse update(Integer userId, Shipping shipping) {
		shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
	}

	@Override
	public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
		Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("更新地址成功",shipping);
	}

	@Override
	public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
		List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
		PageHelper.startPage(pageNum, pageSize);
		PageInfo pageResult = new PageInfo(shippingList);
		return ServerResponse.createBySuccess(pageResult);
	}

}
