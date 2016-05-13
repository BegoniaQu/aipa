package com.aipa.user.module.entity;

import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.domain.GenericIndex;
import com.qy.data.common.rao.annotation.RedisMember;
import com.qy.data.common.rao.annotation.RedisScore;

/**
 * 根据用户ID查用户收藏的帖子
 * 根据帖子ID查被收藏的用户
 * @author qy
 *
 */
@Table(name = "tb_user_note_collect_index")
public class UserNoteCollectIndex implements GenericIndex{

	private static final long serialVersionUID = 4618213799208939556L;

	@Column
	private String indexId;
	
	@Column
	private Integer indexType;
	
	@Column
    @RedisMember
	private Long collectId;
	
	@RedisScore
	private Long score;
	
	@Column
	private Long subScore;
	
	@Column
	private Date createTime;
	
	private static final String[] CONDITIONS_BATCH = new String[] {"indexType", "indexId"};
	private static final String[] CONDITIONS_UNIQUE = new String[] {"indexType", "indexId", "collectId"};
	
	@Override
	public Long getScore() {return score;}
	
	@Override
	public void setScore(Long score) {this.score = score;}

	@Override
	public Date getCreateTime() {return createTime;}

	@Override
	public void setCreateTime(Date createTime) {this.createTime = createTime;}

	@Override
	public Object getHashKey() {return this.indexId;}

	@Override
	public Object getKeyType() {return this.indexType;}
	
	@Override
	public boolean equalIndex(GenericIndex index) {
		UserNoteCollectIndex ix = (UserNoteCollectIndex)index;
        return ix.indexType.equals(indexType)
                && ix.indexId.equals(indexId)
                && ix.collectId.equals(collectId);
	}

	//used to convert from redis to T
	public void setHashKey(Object hashKey){indexId = hashKey.toString(); }
    public void setKeyType(Object keyType){indexType = Integer.valueOf(keyType.toString());}
	
	//根据指定属性查找db，然后zset到同一个key，用于recover redis
	@Override
	public String[] getConditionsOfQueryAll() {return CONDITIONS_BATCH;}

	//根据指定属性可以唯一的删除一个或几个元素
	@Override
	public String[] getConditionsOfQueryUniqueRecord() {return CONDITIONS_UNIQUE;}
	
	@Override
    public String toString() {
        return "DeviceIndex{" +
                "indexType=" + indexType +
                ", indexId='" + indexId + '\'' +
                ", collectId=" + collectId +
                ", score=" + score +
                ", createTime=" + createTime +
                '}';
    }

	public String getIndexId() {return indexId;}

	public void setIndexId(String indexId) {this.indexId = indexId;}

	public Integer getIndexType() {return indexType;}

	public void setIndexType(Integer indexType) {this.indexType = indexType;}

	public Long getCollectId() {return collectId;}

	public void setCollectId(Long collectId) {this.collectId = collectId;}

	public Long getSubScore() {return subScore;}

	public void setSubScore(Long subScore) {this.subScore = subScore;}
}
