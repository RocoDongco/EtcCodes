//java optional  적용법

Map<Integer, String> shopName = new HashMap<>();
shopName.put(1, "11st");
shopName.put(2, "쿠팡");


Optional<String> c = Optional.ofNullable(shopName.get(4)); // 기존이였다면 에러 호출 되는 시점
int length = shopName.map(String::length).orElse(0); //
System.out.println("length: " + length);
