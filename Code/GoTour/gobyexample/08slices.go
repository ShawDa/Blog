package main

import "fmt"

func main() {
	s := make([]string, 3)
	fmt.Println("init s:", s, ", len s:", len(s), ", cap s:", cap(s))

	s[0] = "a"
	s[1] = "b"
	s[2] = "c"
	fmt.Println("set s:", s)
	fmt.Println("get s[2]:", s[2])
	fmt.Println("len s:", len(s))

	s = append(s, "d")
	s = append(s, "e", "f")
	fmt.Println("append s:", s)

	c := make([]string, len(s))
	copy(c, s)
	fmt.Println("copy c:", c)

	l := s[2:5]
	fmt.Println("slice 2-5 l:", l)

	l = s[:5]
	fmt.Println("slice -5 l:", l)

	l = s[2:]
	fmt.Println("slice 2- l:", l)

	t := []string{"g", "h", "i"}
	fmt.Println("init t:", t)

	twoD := make([][]int, 3)
	for i := 0; i < 3; i++ {
		innerLen := i + 1
		twoD[i] = make([]int, innerLen)
		for j := 0; j < innerLen; j++ {
			twoD[i][j] = i + j
		}
	}
	fmt.Println("twoD:", twoD)
}

/*
init s: [  ] , len s: 3 , cap s: 3
set s: [a b c]
get s[2]: c
len s: 3
append s: [a b c d e f]
copy c: [a b c d e f]
slice 2-5 l: [c d e]
slice -5 l: [a b c d e]
slice 2- l: [c d e f]
init t: [g h i]
twoD: [[0] [1 2] [2 3 4]]
*/
