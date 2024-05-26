package com.chat.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
}
