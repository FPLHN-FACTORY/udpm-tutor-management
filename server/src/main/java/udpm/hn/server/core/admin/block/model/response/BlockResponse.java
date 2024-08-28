package udpm.hn.server.core.admin.block.model.response;

import udpm.hn.server.core.common.base.IsIdentify;

public interface BlockResponse extends IsIdentify {

    String getSemesterId();

    String getBlockName();

    Long getStartTime();

    Long getEndTime();

    Integer getBlockStatus();

}
