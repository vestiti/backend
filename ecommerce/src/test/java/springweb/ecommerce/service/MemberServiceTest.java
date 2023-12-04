package springweb.ecommerce.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import springweb.ecommerce.dto.MemberFormDto;
import springweb.ecommerce.entity.Member;
import springweb.ecommerce.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setEmail("test@email.com");
//        memberFormDto.setName("홍길동");
//        memberFormDto.setAddress("서울시 마포구 합정동");
//        memberFormDto.setPassword("1234");

        return Member.builder()
                .email("test@email.com")
                .name("홍길동")
                .address("서울시 마포구 합정동")
                .password("1234")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getAddress()).isEqualTo(savedMember.getAddress());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getRole()).isEqualTo(savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void savedDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        assertThat("이미 가입된 회원입니다.").isEqualTo(e.getMessage());
    }
}