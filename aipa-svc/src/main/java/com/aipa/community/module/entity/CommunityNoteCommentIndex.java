package com.aipa.community.module.entity;

import java.util.Date;

import com.qy.data.common.dao.annotation.Column;
import com.qy.data.common.dao.annotation.Table;
import com.qy.data.common.domain.GenericIndex;
import com.qy.data.common.rao.annotation.RedisMember;
import com.qy.data.common.rao.annotation.RedisScore;

/**
 * 根据帖子ID索引帖子下的评论
 * 根据评论ID索引对评论的回复
 * @author qy
 *
 */
@Table(name="tb_community_note_comment_index")
public class CommunityNoteCommentIndex implements GenericIndex{

	private static final long serialVersionUID = -2881857925191606324L;

	@Column
	private String indexId;
	
	@Column
	private Integer indexType; //1-帖子ID索引评论ID,2-评论ID索引回复ID
	
	@Column
    @RedisMember
	private Long commentId;
	
	@RedisScore
	private Long score;
	
	@Column
	private Long subScore;
	
	@Column
	private Date createTime;
	
	private static final String[] CONDITIONS_BATCH = new String[] {"indexType", "indexId"};
	private static final String[] CONDITIONS_UNIQUE = new String[] {"indexType", "indexId", "commentId"};
	
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
		CommunityNoteCommentIndex ix = (CommunityNoteCommentIndex)index;
        return ix.indexType.equals(indexType)
                && ix.indexId.equals(indexId)
                && ix.commentId.equals(commentId);
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
                ", commentId=" + commentId +
                ", score=" + score +
                ", createTime=" + createTime +
                '}';
    }

	public String getIndexId() {return indexId;}

	public void setIndexId(String indexId) {this.indexId = indexId;}

	public Integer getIndexType() {return indexType;}

	public void setIndexType(Integer indexType) {this.indexType = indexType;}

	public Long getCommentId() {return commentId;}

	public void setCommentId(Long commentId) {this.commentId = commentId;}

	public Long getSubScore() {return subScore;}

	public void setSubScore(Long subScore) {this.subScore = subScore;}
}
