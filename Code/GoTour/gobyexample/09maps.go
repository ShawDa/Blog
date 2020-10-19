package main

import "fmt"

func main() {
	m := make(map[string]int)
	m["k1"] = 7
	m["k2"] = 13
	fmt.Println("map m:", m)
	fmt.Println("len m:", len(m))

	v1 := m["k1"]
	fmt.Println("v1:", v1)

	delete(m, "k2")
	fmt.Println("delete, m:", m)

	_, isExist := m["k2"]
	fmt.Println("isExist, k2:", isExist)

	_, isExist = m["k1"]
	fmt.Println("isExist, k1:", isExist)

	n := map[string]int{"foo": 1, "bar": 2}
	fmt.Println("n:", n)
}

/*
map m: map[k1:7 k2:13]
len m: 2
v1: 7
delete, m: map[k1:7]
isExist, k2: false
isExist, k1: true
n: map[bar:2 foo:1]
*/
