package com.udacity.jwdnd.course1.cloudstorage.mapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findFile(int fileId);

    @Insert("INSERT INTO FILES (fileId, fileName, contentType, fileSize, userId, fileData) VALUES(#{fileId}, #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int addFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId} AND filename = #{filename}")
    File uniqueFileName(Integer userId, String filename);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getFilesByUserId(int userId);
}
