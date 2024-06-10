package org.example.spartaboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.spartaboard.dto.ProfileModifyRequestDto;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name="user")
public class User extends Timestamped{

    @Id //찾을 때 추천(고유)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank//가입시 필수
    @Column(nullable = false, unique = true)
    private String userId;

    @NotBlank//가입시 필수
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column
    private String refreshToken;

    //상태 변경 시간
    //userEntity 에만 필요하므로 User 에 위치시킴
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime StatusChangedAt;

    @OneToMany
    List<Post2> PostList = new ArrayList<>();

    //refreshtoken 생성시간 을 테이블에 넣으라는 소리는 없던데..
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime RefreshCreatedAt;

    private RefreshTokenTime refreshTokenUsage;

    @JoinColumn(name = "expired_token")
    @OneToOne(fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    public void update(ProfileModifyRequestDto requestDto) {
        if (requestDto.getUsername() != null) {
            this.username = requestDto.getUsername();
        }
        if (requestDto.getIntroduce() != null) {
            this.introduce = requestDto.getIntroduce();
        }
        if (requestDto.getNewPassword() != null) {
            this.password = requestDto.getNewPassword();
        }

    }

    public void changeStatus() {
        this.status = UserStatus.INACTIVE;
    }
}
