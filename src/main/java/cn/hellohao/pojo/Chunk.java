package cn.hellohao.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class Chunk implements Serializable {
    @Id
    private String uid;
    /**
     * 当前文件块，从1开始
     */
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    private Long currentChunkSize;
    /**
     * 总大小
     */
    private Long totalSize;
    /**
     * 文件标识
     */
    private String identifier;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 相对路径
     */
    private String relativePath;
    /**
     * 总块数
     */
    private Integer totalChunks;
    /**
     * 文件类型
     */
    private String type;

    /**
     * 前台创建的随机UID,用作创建合并文件的唯一标识目录名
     */
    private String uuid;

    /**
     * 归类
     */
    private String classifications;

    /**
     * 天数
     */
    private Integer day;


    @Transient
    private MultipartFile file;

}