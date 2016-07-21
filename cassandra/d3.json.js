 var data = [
        {"name": "Maria", "age" : 30},    
    {"name": "Fred", "age" : 50},    
    {"name": "Francis", "age" : 12}
    ];

var csv = "Fred,50\nMaria,30\nFrancis,12";

//loading files
//d3.csv()
//d3.json("mydata.json", function(data){
d3.json("mydata.json", function(data){
    var canvas = d3.select("body").append("svg")
    .attr("width", 500)
    .attr("height", 500);

    canvas.selectAll("rect")
    .data(data)
    .enter()
    .append("rect")
    .attr("width", function(d){
        return d.age;
    })
    .attr("height", 50)
    .attr("y", function(d, i){
        return i * 50;
    })
    .attr("fill", "blue")

    canvas.selectAll("text")
    .data(data)
    .enter()
    .append("txt")
    .attr("fill", "white")
    .attry("y", function(d, i){ return i * 50 + 24})
    .text(function(d){return d.name; })
});
    //data.shift() -> get first element
    //data.sort(d3.descending)
    //d3.min(data)
    //d3.max(data)
    //d3.extent(data) both min and max
    //d3.sum(data)
    //d3.mean(data)
    //d3.median(data)
    //d3.shuffle(data)