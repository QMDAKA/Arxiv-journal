/*<![CDATA[*/

console.log(jsonString);
window.onload = function () {
  var result = JSON.parse(jsonString);
  var weighted_predict = result[0];
  console.log(weighted_predict);
  var normal_predict = result[1];
  for(var i = 0; i < normal_predict.matrix.length; i++) {
    var matrix = normal_predict.matrix;
    var idChart = "chartContainer" + i;
    var chart = new CanvasJS.Chart(idChart, {

      theme: "light2",
      animationEnabled: true,
      title: {
        text: "FOLD "+i
      },
      data: [{
        type: "pie",
        indexLabelFontSize: 18,
        radius: 80,
        indexLabel: "{label} - {y}",
        yValueFormatString: "###0.0\"%\"",
        click: explodePie,
        dataPoints: [
          {y: (matrix[i].tp/matrix[i].size)*100, label: "True Positive"},
          {y:(matrix[i].tn/matrix[i].size)*100 , label: "True Negative"},
          {y: (matrix[i].fp/matrix[i].size)*100, label: "False Positive"},
          {y:(matrix[i].fn/matrix[i].size)*100, label: "False Negative"}
        ]
      }]
    });
    chart.render();
  }
  for( i = 0; i < weighted_predict.matrix.length; i++) {
     matrix = weighted_predict.matrix;
     idChart = "chartContainer" + i + "weigh";
     chart = new CanvasJS.Chart(idChart, {

      theme: "light2",
      animationEnabled: true,
      title: {
        text: "FOLD "+i
      },
      data: [{
        type: "pie",
        indexLabelFontSize: 18,
        radius: 80,
        indexLabel: "{label} - {y}",
        yValueFormatString: "###0.0\"%\"",
        click: explodePie,
        dataPoints: [
          {y: (matrix[i].tp/matrix[i].size)*100, label: "True Positive"},
          {y:(matrix[i].tn/matrix[i].size)*100 , label: "True Negative"},
          {y: (matrix[i].fp/matrix[i].size)*100, label: "False Positive"},
          {y:(matrix[i].fn/matrix[i].size)*100, label: "False Negative"}
        ]
      }]
    });
    chart.render();
  }
}

function explodePie (e) {
  if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
    e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
  } else {
    e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
  }
  e.chart.render();

}
/*]]>*/
