package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.domain.model.Flower;

import java.util.List;
import java.util.Optional;

/**
 * [Domain Layer] 鲜花存储库接口
 * 定义鲜花相关的持久化操作契约
 */
public interface FlowerRepository {

    /**
     * 获取所有公开展示的鲜花 (用于首页/图鉴)
     */
    List<Flower> findAllPublic();

    /**
     * 保存/上架鲜花
     * @param sellerId 卖家 ID
     * @param dto 前端提交的鲜花信息
     */
    void save(String sellerId, FlowerDTORequest dto);

    /**
     *  [新增] 查询指定卖家的库存列表
     * (对应 JdbcFlowerRepository.findAllBySellerId)
     */
    List<Flower> findAllBySellerId(String sellerId);

    /**
     * [新增] 查询鲜花详情（包含卖家公开信息）
     * (对应 JdbcFlowerRepository.findDetailById)
     */
    Optional<FlowerDetailDTOResponse> findDetailById(Long id);
}