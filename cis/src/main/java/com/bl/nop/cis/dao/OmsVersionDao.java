package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.version.Version;
import com.bl.nop.entity.version.VersionDownloader;

public interface OmsVersionDao {
    List<Version> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);

    List<Version> queryOutVersion(Map<String, Object> map);

    List<VersionDownloader> findDownloaderItemByPage(Map<String, Object> param);

    int findDownloaderItemCount(Map<String, Object> param);

}
