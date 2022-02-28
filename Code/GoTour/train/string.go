package main

import (
	"fmt"
	"strconv"
	"strings"
)

func main() {
	helloWorld := "hello世界"
	// 英文数字占一个字节，汉字占三个字节
	fmt.Println(len(helloWorld)) // 11

	everyChar := []rune(helloWorld)
	for i := 0; i < len(everyChar); i++ {
		// h e l l o 世 界
		fmt.Printf("%c\n", everyChar[i])
	}

	atoi, err := strconv.Atoi("100")
	if err != nil {
		fmt.Println("conv error")
	} else {
		fmt.Println(atoi) // 100
	}

	itoa := strconv.Itoa(1000)
	fmt.Println(itoa) // 1000

	bytes := []byte("abc def")
	fmt.Printf("%v\n", bytes) // [97 98 99 32 100 101 102]

	s := string([]byte{99, 98, 97, 96})
	fmt.Println(s) // cba`

	println(strconv.FormatInt(123, 2)) // 1111011

	println(strconv.FormatInt(123, 8)) // 173

	println(strconv.FormatInt(123, 16)) // 7b

	println(strings.Contains("abccba", "bccb")) // true

	println(strings.Count("abccbaabccba", "a")) // 4

	fmt.Printf("%v\n", strings.EqualFold("AbC", "abc")) // true

	fmt.Println(strings.Index("abccba", "cba")) // 3

	fmt.Println(strings.LastIndex("go golang", "go")) // 3

	// golang golang go please
	fmt.Println(strings.Replace("go go go please", "go", "golang", 2))

	// [go go go please]
	fmt.Println(strings.Split("go,go,go,please", ","))

	fmt.Println(strings.ToLower("Go")) // go
	fmt.Println(strings.ToUpper("Go")) // GO

	fmt.Println(strings.TrimSpace(" ab cd ")) // ab cd

	fmt.Println(strings.Trim("!ab!cd!!", "!")) // ab!cd

	fmt.Println(strings.HasPrefix("http://10.1.1.1", "http")) // true

	fmt.Println(strings.HasSuffix("time.png", "jpg")) // false
}
