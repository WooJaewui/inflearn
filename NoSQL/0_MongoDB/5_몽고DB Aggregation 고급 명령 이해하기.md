
# 몽고DB Aggregation 고급 명령 이해하기

------------------------------------------------------------------------------------------------------------------------

> ## MongoDB Aggregation 고급 명령 이해하기1

### $project
    {$project: {_id:0, title:1, ...}}
- 출력할 필드를 결정하는 것


### $concat 새로운 필드 생성하기
    // 예시 코드
    {$project: {
        _id:0
        , title: 1
        , newColumn: {$concat: ["$title", "$"]}
    }}


### $lookup
    {$lookup: {
        from: <collection>
        , localField: <field>
        , foreignField: <field>
        , as : <newField>
    }}
- 두 컬렉션을 매칭하는 것 (SQL join과 비슷)
- from : 조인할 컬렉션의 이름
- localField: 현재 컬렉션에서 조인에 사용할 필드
- foreignField: 조인할 컬렉션에서 조인에 사용할 필드

------------------------------------------------------------------------------------------------------------------------

> ## MongoDB Aggregation 고급 명령 이해하기2

### 영어단어
- facet : 면
- lookup : 조회, 찾다


### skip
    {$skip: 3}
- document 건너 뜀


### $facet
    {$facet: {
        <outputField1>: [<stage1>, <stage2>, ...]
        <outputField2>: [<stage3>, <stage4>, ...]
    }}
- 파이프라인 내에서 다중 Aggregation 파이프 라인을 정의하여 여러 결과 집합을 생성
- outputField : 각각의 결과 집합에 대한 출력 필드 이름
- stage : 각각의 결과 집합을 생성하기 위한 Aggregation 스테이지


### $redact
    {$redact: {
        $cond: {
            if: <condition>
            , then: <expression1>
            , else: <expression2>
        }
    }
- document 내에서 보안이나 필터링 규칙에 따라 문서를 제어하는 데 사용된다 
- condition : 조건
- expression1 : condition이 true일 때 적용되는 표현식
- expression2 : condition이 false일 때 적용되는 표현식
    - $$KEEP : document를 보존하고 다음 하위 document를 검사
    - $$PRUNE : document를 제거하고 추가 검토를 중지
















