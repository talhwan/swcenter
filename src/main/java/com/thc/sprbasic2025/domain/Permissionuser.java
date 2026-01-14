package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PermissionuserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(
        indexes = {
                @Index(columnList = "deleted")
                ,@Index(columnList = "permissionId")
                ,@Index(columnList = "userId")
        }
        ,uniqueConstraints = {@UniqueConstraint(
        name = "UQ_permissionuser_permissionId_userId"
        ,columnNames = {"permissionId", "userId"}
        )}
)
@Entity
public class Permissionuser extends AuditingFields{
    Long permissionId;
    Long userId;

    protected Permissionuser(){}
    private Permissionuser(Long permissionId, Long userId){
        this.permissionId = permissionId;
        this.userId = userId;
    }
    public static Permissionuser of(Long permissionId, Long userId){
        return new Permissionuser(permissionId, userId);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }

    public void update(PermissionuserDto.UpdateReqDto param) {
        if(param.getDeleted() != null){ setDeleted(param.getDeleted()); }
        if(param.getUserId() != null){ setUserId(param.getUserId()); }
    }

}
