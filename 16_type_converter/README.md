# Spring Type Converter
###### 문자를 숫자로 변경하거나, 숫자를 문자로 변경하는 순간의 일을 타입 컨버터라고 한다.

### Converter interface
- org.springframework.core.convert.converter.Converter
- [다양한 컨버터 종류](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#core-convert)
- > Converter 기본 타입 컨버터
- > ConverterFactory 전체 클래스 계층 구조가 필요할 때
- > GenericConverter 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
- > ConditionalGenericConverter 특정 조건이 참인 경우에만 실행
- 사실 이미 스프링은 문자, 숫자, boolean, Enum등 일반적인 타입에 대한 대부분의 컨버터가 이미 기본으로 제공되고 있다. Converter의 구현체를 찾아보면 되겠다.
```java
@FunctionalInterface
public interface Converter<S, T> {
    @Nullable
    T convert(S source);

    default <U> Converter<S, U> andThen(Converter<? super T, ? extends U> after) {
        Assert.notNull(after, "'after' Converter must not be null");
        return (s) -> {
            T initialResult = this.convert(s);
            return initialResult != null ? after.convert(initialResult) : null;
        };
    }
}
```

### ConversionService
- ConversionService, 예를들어 DefaultConversionService 를 사용하면 **등록과 사용을 분리**할 수 있다.
- 등록과 사용을 분리하는 개념은 스프링 OOP 설계에서 상당히 중요한 부분이다.
##### 인터페이스 분리 원칙 - ISP(Interface Segregation Principle)
- 인터페이스 분리 원칙은 클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다.
##### DefaultConversionService 는 다음 두 인터페이스를 구현했다.
- ConversionService : 컨버터 사용에 초점
- ConverterRegistry : 컨버터 등록에 초점

### 스프링에 Converter 적용하기
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToIntegerConverter());
    registry.addConverter(new IntegerToStringConverter());
    registry.addConverter(new StringToIpPortConverter());
    registry.addConverter(new IpPortToStringConverter());
  }
}
```
- 스프링은 내부에서 ConversionService 를 제공한다.
- WebMvcConfigurer 가 제공하는 addFormatters() 를 사용해서 추가하고 싶은 컨버터를 등록하면 된다.
- addFormatters()에 사실 추가해주지 않아도, 왠만한 Converter는 다 구현되어 있기 때문에 파라미터 컨버팅이 잘 일어난다.

### Tymeleaf 와 Converter
- 변수 표현식 : ${...}
- Conversion Service 적용 : ${{...}}
- Typeleaf의 th:field 는 컨버전 서비스도 default로 함께 적용된다.

### Formatter
- 포맷터( Formatter )는 객체를 문자로 변경하고, 문자를 객체로 변경하는 두 가지 기능을 모두 수행한다.
```java
// 객체를 문자로 변경한다.
String print(T object, Locale locale)
// 문자를 객체로 변경한다
T parse(String text, Locale locale)
```
- Formatter interface
```java
public interface Printer<T> {
  String print(T object, Locale locale);
}

public interface Parser<T> {
  T parse(String text, Locale locale) throws ParseException;
}

public interface Formatter<T> extends Printer<T>, Parser<T> {
}
```

### Converter vs Formatter
- Converter 는 범용(객체 -> 객체)
- Formatter 는 문자에 특화(객체 -> 문자, 문자 -> 객체) + 현지화(Locale)

### DefaultFormattingConversionService
- 이 클래스를 사용하면 Formater 와 Converter 의 기능을 둘다 함께 등록해서 사용할 수 있다.

### 스프링에 Formatter 적용하기
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addFormatters(FormatterRegistry registry) {
      //주석처리 우선순위
      //registry.addConverter(new StringToIntegerConverter());
      //registry.addConverter(new IntegerToStringConverter());
      registry.addConverter(new StringToIpPortConverter());
      registry.addConverter(new IpPortToStringConverter());
      //추가
      registry.addFormatter(new MyNumberFormatter());
  }
}
```

### 스프링이 제공하는 기본 포맷터
- @NumberFormat : 숫자 관련 형식 지정 포맷터 사용, NumberFormatAnnotationFormatterFactory
- @DateTimeFormat : 날짜 관련 형식 지정 포맷터 사용, Jsr310DateTimeFormatAnnotationFormatterFactory
