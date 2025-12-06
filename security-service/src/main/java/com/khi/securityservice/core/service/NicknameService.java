package com.khi.securityservice.core.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class NicknameService {

    private final List<String> adjectives = List.of(
            "즐거운", "행복한", "씩씩한", "용감한", "고요한", "포근한", "따스한", "차분한",
            "말랑한", "푸른", "맑은", "반짝이는", "배고픈", "졸린", "심심한", "수상한",
            "엉뚱한", "똑똑한", "느긋한", "성실한", "지루한", "상쾌한", "배부른", "화려한",
            "소심한", "대담한", "냉철한", "다정한", "친절한", "수줍은"
    );

    private final List<String> nouns = List.of(
            "쿼카", "수달", "판다", "고양이", "강아지", "토끼", "다람쥐", "알파카",
            "펭귄", "햄스터", "베이글", "푸딩", "젤리", "감자", "옥수수", "치즈",
            "쿠키", "만두", "구름", "바다", "노을", "햇살", "우주", "로켓",
            "여행자", "탐험가", "개발자", "기획자", "대장", "마법사", "히어로", "고수"
    );

    private final Random random = new Random();

    /**
     * 랜덤 닉네임 생성
     * @return "형용사 명사" 형태의 문자열 (예: "배고픈 쿼카")
     */
    public String generateRandomNickname() {
        String adj = adjectives.get(random.nextInt(adjectives.size()));
        String noun = nouns.get(random.nextInt(nouns.size()));

        // 띄어쓰기 없이 붙여쓰려면 공백 제거: return adj + noun;
        return adj + " " + noun;
    }
}
