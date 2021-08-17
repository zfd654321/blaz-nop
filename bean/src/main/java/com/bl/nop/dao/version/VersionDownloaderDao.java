package com.bl.nop.dao.version;

import com.bl.nop.entity.version.VersionDownloader;

public interface VersionDownloaderDao {
    int deleteByPrimaryKey(String id);

    int insert(VersionDownloader record);

    int insertSelective(VersionDownloader record);

    VersionDownloader selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(VersionDownloader record);

    int updateByPrimaryKey(VersionDownloader record);
}