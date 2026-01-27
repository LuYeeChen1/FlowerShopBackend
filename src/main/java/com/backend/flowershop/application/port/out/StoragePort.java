package com.backend.flowershop.application.port.out;

import java.net.URL;

public interface StoragePort {
    // 生成上传凭证
    URL generatePresignedUrl(String key, String contentType);
}