package main

import (
	"fmt"
	"sort"
)

func main() {
	makeMap := make(map[string]string, 10)
	makeMap["a"] = "aa"
	makeMap["b"] = "bb"
	makeMap["c"] = "cc"
	makeMap["b"] = "bbb"
	fmt.Println(makeMap) // map[a:aa b:bbb c:cc]

	films := map[string]string{
		"Captain American": "1",
		"Hulk":             "2",
	}
	films["Blcak Widow"] = "333"
	fmt.Println(films) // map[Blcak Widow:333 Captain American:1 Hulk:2]

	delete(makeMap, "z")
	fmt.Println(makeMap) // map[a:aa b:bbb c:cc]
	delete(makeMap, "c")
	fmt.Println(makeMap) // map[a:aa b:bbb]

	for k, _ := range makeMap {
		delete(makeMap, k)
	}
	fmt.Println(makeMap) // map[]

	// map slice, 一个animal对应一个map
	animals := make([]map[string]string, 2)
	animals[0] = make(map[string]string, 2)
	animals[0]["name"] = "cat"
	animals[0]["age"] = "2"
	animals[1] = make(map[string]string, 2)
	animals[1]["name"] = "rabbit"
	animals[1]["age"] = "4"

	newAnimal := map[string]string{
		"name": "dog",
		"age":  "3",
	}
	newAnimals := append(animals, newAnimal)
	fmt.Println(animals)    // [map[age:2 name:cat] map[age:4 name:rabbit]]
	fmt.Println(newAnimals) // [map[age:2 name:cat] map[age:4 name:rabbit] map[age:3 name:dog]]

	myMap := make(map[int]int)
	myMap[110] = 100
	myMap[1] = 13
	myMap[4] = 1
	myMap[8] = 1000
	fmt.Println(myMap) // map[1:13 4:1 8:1000 110:100]

	var keys []int
	var values []int
	for k, v := range myMap {
		keys = append(keys, k)
		values = append(values, v)
	}
	sort.Ints(keys)
	fmt.Println(keys) // [1 4 8 110]
	for _, key := range keys {
		fmt.Println(myMap[key]) // 13 1 1000 100
	}
}
