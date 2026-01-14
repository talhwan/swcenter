package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PermissiondetailDto;
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
                ,@Index(columnList = "target")
                ,@Index(columnList = "func")
        }
        ,uniqueConstraints = {@UniqueConstraint(
        name = "UQ_permissiondetail_permissionId_target_func"
        ,columnNames = {"permissionId", "target", "func"}
        )}
)
@Entity
public class Permissiondetail extends AuditingFields{
    Long permissionId;
    @Column(length = 100) String target; // 어떤 테이블에 해당하는지! user notice
    Integer func; // 어떤 기능을 할지 create 110 read 200 update 120

    protected Permissiondetail(){}
    private Permissiondetail(Long permissionId, String target, Integer func){
        this.permissionId = permissionId;
        this.target = target;
        this.func = func;
    }
    public static Permissiondetail of(Long permissionId, String target, Integer func){
        return new Permissiondetail(permissionId, target, func);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }

    public void update(PermissiondetailDto.UpdateReqDto param) {
        if(param.getDeleted() != null){ setDeleted(param.getDeleted()); }
        if(param.getTarget() != null){ setTarget(param.getTarget()); }
        if(param.getFunc() != null){ setFunc(param.getFunc()); }
    }

}
