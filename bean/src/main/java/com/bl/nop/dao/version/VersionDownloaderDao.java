package com.bl.nop.dao.version;

import com.bl.nop.entity.version.VersionDownloader;

public interface VersionDownloaderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionDownloader record);

    int insertSelective(VersionDownloader record);

    VersionDownloader selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionDownloader record);

    int updateByPrimaryKey(VersionDownloader record);
}