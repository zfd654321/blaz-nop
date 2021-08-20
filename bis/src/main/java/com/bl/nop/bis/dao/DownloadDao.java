package com.bl.nop.bis.dao;

import java.util.Map;

import com.bl.nop.entity.version.Version;

import org.springframework.stereotype.Repository;

@Repository
public interface DownloadDao {

    Version queryRunVersion(Map<String, Object> map);

}
