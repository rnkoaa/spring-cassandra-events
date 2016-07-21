//Part One
        /*d3.select("p").text("Hello, World!");
        d3.select("body")
        .append("span")
        .style("color", "red")
        .text("Hi, What's up?");*/

        //Part two - SVG
        /*var canvas = d3.select("body")
        .append("svg")
        .attr("width", 500)
        .attr("height", 500);

        var circle = canvas.append("circle")
        .attr("cx", 250)
        .attr("cy", 250)
        .attr("r", 50)
        .attr("fill", "red");

        var rect = canvas.append("rectangle")
        .attr("width", 100)
        .attr("height", 50)
        .attr("fill", "blue");

        var line = canvas.append("line")
        .attr("x1", 0)
        .attr("y1", 100)
        .attr("x2", 400)
        .attr("y2", 400)
        .attr("stroke", "green")
        .attr("stroke-width", 2);*/

        //Part three - Bind Data 
        var dataArray = [20, 40, 50, 60];

        var width = 500;
        var height = 500;

var widthScale = d3.scale.linear()
        .domain([0, 60])
        .range([0, width]);

var color =d3.scale.linear()
            .domain([0, 60])
            .range(["red", "blue"]);

var axis = d3.svg.axis()
            .ticks(5)
            .scale(widthScale);

        var canvas = d3.select("body")
        .append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        //move the element
        .attr("transform", "translate(20, 0)")
        //.call(axis);

        var bars = canvas.selectAll("rect")
        .data(dataArray)
        .enter() 
        .append("rect")
        //for each data element, we return 
        .attr("width", function(d){
            return widthScale(d);
        })
        .attr("height", 50)
        .attr("fill", function(d){
            return color(d);
        })
        .attr("y", function(d,i){
            return i *100;
        });

        canvas.append("g")
        .attr("transform", "translate(0, 400)")
        .call(axis);



//================New One=============

var data = [0];

        var canvas = d3.select("body")
        .append("svg")
        .attr("width", 500)       
        .attr("height", 500)  ;

var circle1 = canvas.append("circle")
        .attr("cx", 50)
        .attr("cy", 100) 
       // .attr("fill", "blue")
        .attr("r", 25);  

var circle2 = canvas.append("circle")
        .attr("cx", 50)
        .attr("cy", 100) 
       // .attr("fill", "blue")
        .attr("r", 25);  

var circles = canvas.selectAll("circle")
        .data(data)
        .attr("fill", "red")
        .enter()
        .append("circle")
        .attr("cx", 50)
        .attr("cy", 50) 
        .attr("fill", "green")
        .attr("r", 25);   


//================Another=============
    /**DOM Elements < data elements (enter)
         * DOM elements > data elements (exit)
         * DOM elements = data elements (update) */ 

var data = [0];

        var canvas = d3.select("body")
        .append("svg")
        .attr("width", 500)       
        .attr("height", 500)  ;

var circle = canvas.append("circle")
        .attr("cx", 50)
        .attr("cy", 200) 
       // .attr("fill", "blue")
        .attr("r", 25);  

/*var circles = canvas.selectAll("circle")
        .data(data)
        .attr("fill", "green") 
        .exit()
            .attr("fill", "blue");*/

       /* circle.transition()
        .duration(1500)
        .delay(500)
        .attr("cx", 150)
        .transition()
        .attr("cy", 400);*/
        circle.transition()
        .duration(1500)
        .attr("cx", 150)
        .each("end", function(){
            d3.select(this).attr("fill", "red");
        })
        /*.each("start", function(){
            d3.select(this).attr("fill", "red");
        })*/