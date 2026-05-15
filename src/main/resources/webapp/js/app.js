var socket;
var searchKey;
var searchAuditKey;
var searchKeyAudit;
var backLogs;
var backLogChartLabels = ["Pending","Processed", "Total Received"];
var imgReviewStatusPlotLabels = ["VSR","1st Review", "2nd Review", "Code Off Recommended", "Supervisor Review", "Received"];
var imgReviewCompletedStatusBarChartLabels = ["TR Completed", "Code Off", "Completed", "Auto Completed", "Total"];
var autoImageVSmanualRewiewLabels = ["Double MR", "Single MR", "Auto Completed", "Total"];

function disableWrappers(){
   var selection= ['dashboard', 'status', 'audit', 'performance', 'backlog'];
  
   for (var i in selection) {
       $('#' + selection[i]+'-wrapper').hide();
       $('#' + selection[i]+'-button').removeClass('active');
   }
}

function displayWrapper( selected ){
    disableWrappers();
    $('#'+selected+'-wrapper').show();
    $('#' + selected+'-button').addClass('active');
	console.log(selected);
    if(selected === "dashboard"){
		plotBackLogBarChart(backLogs);
        $('#pageTitle').html("Image Reviewer Dashboard");
    }else if(selected === "status"){
        $('#pageTitle').html("Image Transaction Status Dashboard");
    }else if(selected === "audit"){
        $('#pageTitle').html("Image Review Audit Dashboard");
    }
}

function initLoad(){
	if(window.location.protocol === 'https:') {
		server =  "wss://" + window.location.host + window.location.pathname + "ips-ws" ;
	} else {
		server =  "ws://" + window.location.host + window.location.pathname + "ips-ws" ;
	}
	console.log('call initLoad to server ' + server);
	socket = new WebSocket(server);

	socket.onmessage = onMessage;
	$('#srvStatus').hide();
	$('#msg').hide();
	$('#srvAuditStatus').hide();
	$('#msgAudit').hide();
	$("#submitBtn").click(function(){
       getDailyReviewStats();
	});
	
	$("#submitAuditBtn").click(function(){
       getAudit();
	});
	
		$('#backLogChart').bind('jqplotDataHighlight', 
            function (ev, seriesIndex, pointIndex, data) {
                $('#info1').html(backLogChartLabels[seriesIndex]+' - '+data[1]);
            }
        );
             
        $('#backLogChart').bind('jqplotDataUnhighlight', 
            function (ev) {
                $('#info1').html('');
            }
        );
	
		$('#imgReviewStatusChart').bind('jqplotDataHighlight', 
            function (ev, seriesIndex, pointIndex, data) {
                $('#info2').html(imgReviewStatusPlotLabels[seriesIndex]+' - '+data[1]);
            }
        );
             
        $('#imgReviewStatusChart').bind('jqplotDataUnhighlight', 
            function (ev) {
                $('#info2').html('');
            }
        );
		
		$('#imgReviewCompletedStatusChart').bind('jqplotDataHighlight', 
            function (ev, seriesIndex, pointIndex, data) {
                $('#info3').html(imgReviewCompletedStatusBarChartLabels[seriesIndex]+' - '+data[1]);
            }
        );
             
        $('#imgReviewCompletedStatusChart').bind('jqplotDataUnhighlight', 
            function (ev) {
                $('#info3').html('');
            }
        );
		
		$('#autoImageVSmanualRewiew').bind('jqplotDataHighlight', 
            function (ev, seriesIndex, pointIndex, data) {
                $('#info4').html(autoImageVSmanualRewiewLabels[seriesIndex]+' - '+data[1]);
            }
        );
             
        $('#autoImageVSmanualRewiew').bind('jqplotDataUnhighlight', 
            function (ev) {
                $('#info4').html('');
            }
        );
}

function getDailyReviewStats(){
	var start = new Date().getTime();
	var facility = $('#facility').val();
	var date = $('#datepicker').val();
	$('#msg').hide();
	$('#srvStatus').show();
	//var url = 'http://hbbldas02:8080/IpsDashBoard/resources/txn/'+facility+'/'+date;
        var server =  window.location.host + window.location.pathname ;
        var url = 'http://'+server+'resources/txn/'+facility+'/'+date;
	console.log('sending request to '+url);
	  $.ajax({
		url:url,
		type:'GET',
		dataType: 'json',
		success: function( json ) {
			var end = new Date().getTime();
			var time = end - start;
			var sec = time/1000;
			console.log('Daily Review Stats Data');
			console.log(json.dailyReviewStats);
			$('#srvStatus').hide();
			$('#msg').text('Data Processing Time: '+sec+' sec');
			$('#msg').show();
			plotImageReviewStatusBarChart(json.dailyReviewStats);
			plotImageReviewCompletedStatusBarChart(json.dailyReviewStats);
			plotAutoVSManualImageBarChart(json.dailyReviewStats);
		}
	});
}

function getAudit(){
	var start = new Date().getTime();
	var date = $('#datepickerAudit').val();
	$('#msgAudit').hide();
	$('#srvAuditStatus').show();
	//var url = 'http://hbbldas02:8080/IpsDashBoard/resources/audit/'+date;
        var server =  window.location.host + window.location.pathname ;
        var url = 'http://'+server+'resources/audit/'+date;
	console.log('sending request to '+url);
	  $.ajax({
		url:url,
		type:'GET',
		dataType: 'json',
		success: function( json ) {
			var end = new Date().getTime();
			var time = end - start;
			var sec = time/1000;
			console.log('Daily Review Stats Data');
			console.log(json);
			$('#srvAuditStatus').hide();
			$('#msgAudit').text('Data Processing Time: '+sec+' sec');
			$('#msgAudit').show();
			loadAuditSummary(json.auditSummaryCol);
			loadTruthSetSummary(json.truthRunCol);
		}
	});
}

function loadTruthSetSummary(data){
	$('#truthSetSummaryTable').DataTable().destroy();
	var table = $('#truthSetSummaryTable').DataTable({
		filter : true,
		ordering : true,
		lengthChange: false,
		paging: true,
		pageLength: 4,
		info: false,
        data: data,
		columnDefs: [
					
						{ "width": "5%", "targets": 6 },
						{ "width": "5%", "targets": 7 }
					],
		columns: [
					{ data: 'truthSetName' },
					{ data: 'initiatedUserId' },
					{ data: 'userId' },
					{ data: 'createdDate' },
					{ data: 'runDate' },
					{ data: 'status' },
					{ data: 'completePercent' },
					{ data: 'accuracyPercent' }
				]
	});
}


function loadAuditSummary(data){
	$('#reviewerAuditSummaryTable').DataTable().destroy();
	var table = $('#reviewerAuditSummaryTable').DataTable({
		filter : true,
		ordering : true,
		lengthChange: false,
		paging: true,
		pageLength: 5,
		info: false,
        data: data,
		columns: [
					{ data: 'auditorName' },
					{ data: 'reviewerName' },
					{ data: 'auditDate' },
					{ data: 'startDate' },
					{ data: 'endDate' },
					{ data: 'auditItemCount' },
					{ data: 'passedCount' },
					{ data: 'failedCount' },
					{ data: 'skippedCount' },
					{ data: 'failPercent' },
					{ data: 'passPercent' }
				]
	});
}

function onMessage(event) {
	console.log("New message is comming ...");
    var payLoad = JSON.parse(event.data);
	console.log(payLoad);
	$('#updateTime').text(payLoad['refreshTime']);
	$('#totalBacklog').text(payLoad['totalBacklog']);
    var userStats = payLoad['userStats'];
	backLogs = payLoad['backLogDays'];
	var qStats = payLoad['queueStatsList'];
	loadSmallTable(payLoad['ipsTimeHorizon75A'],payLoad['ipsTimeHorizon75B'], 'timeHorizon');
	
	loadSmallTable(payLoad['ipsTimeHorizon75A'],payLoad['ipsTimeHorizon75B'], 'timeHorizonAudit');
	
	loadSmallTable(payLoad['backlog75A'], payLoad['backlog75B'], 'backLogAudit');
	
	loadQStats(qStats);
	loadReviewers(userStats,'reviewers',4);
	loadReviewers(userStats,'reviewersAudit',8);
	plotBackLogBarChart(backLogs);
	loadBackLog(backLogs);
}

function loadBackLog(backLogs){
	console.log('loadBackLog');
	console.log(backLogs);
	$('#backLogTable').DataTable().destroy();
	var col_name = {title:'Type', data:'type'};
	var col_names = [];
	col_names.push(col_name);
	var data = [];
	data.push({type:'Pending'});
	data.push({type:'Processed'});
	data.push({type:'Total Received'});
	for (var i =0; i<backLogs.length; i++) {
		var new_field = 'date_'+i;
		col_name = {title:backLogs[i].backlogDate, data:new_field};
		col_names.push(col_name);
		data[0][new_field] = backLogs[i].pending;
		data[1][new_field] = backLogs[i].processed;
		data[2][new_field] = backLogs[i].totalReceived;
	}

	var table = $('#backLogTable').DataTable({
		filter : false,
		ordering : false,
		lengthChange: false,
		paging: false,
		info: false,
        columns: col_names,
		columnDefs: [
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" },
						{ "width": "11%" }
					],
		data:data
	});
}

function loadSmallTable(a,b,elem){
	var data = [];
	var facilityA = {facility:'75 A', timeHorizon:a};
	var facilityB = {facility:'75 B', timeHorizon:b};
	data.push(facilityA);
	data.push(facilityB);
	$('#'+elem).DataTable().destroy();
	var table = $('#'+elem).DataTable({
		filter : false,
		ordering : false,
		lengthChange: false,
		paging: false,
		info: false,
		columnDefs: [
						{ "width": "30%", "targets": 0 },
						{ "width": "70%", "targets": 1 }
					],
        data:data,
		columns: [
					{ data: 'facility' },
					{ data: 'timeHorizon' }
				]
	});
	
}

function loadQStats(qStats){
	$('#qStats').DataTable().destroy();
	var table = $('#qStats').DataTable({
		filter : false,
		ordering : true,
                order: [[ 1, "asc" ]],
		lengthChange: false,
		paging: false,
		info: false,
		columnDefs: [
						{ "width": "30%", "targets": 0 },
						{ "width": "50%", "targets": 1 },
						{ "width": "20%", "targets": 2 }
					],
        data:qStats,
		columns: [
					{ data: 'queueName' },
					{ data: 'minDate' },
					{ data: 'queueLength' }
				]
	});
}

function loadReviewers(userStats, elem, size){
	$('#'+elem).DataTable().destroy();
	var table = $('#'+elem).DataTable({
		filter : true,
		ordering : true,
		lengthChange: false,
		info: false,
        pageLength: size,
		search: {
    search: searchKey
  },
		columnDefs: [
						{ "width": "25%", "targets": 0 },
						{ "width": "29%", "targets": 1 },
						{ "width": "23%", "targets": 2 },
						{ "width": "23%", "targets": 3 }
					],
        data:userStats,
		columns: [
					{ data: 'userId' },
					{ data: 'codeOffCount' },
					{ data: 'reviewCount' },
					{ data: 'totalCount' }
				]
	});

	$("input").keyup(function(){
	   searchKey=$("input").val();
});
}

function plotBackLogBarChart(backLogData){
	console.log('plotBackLogBarChart');
	var pending = [];
    var s2 = [];
	var s3 = [];
    var ticks = [];
	
	for (var i =0; i<backLogData.length; i++) {	
		pending.push(backLogData[i].pending);
		s2.push(backLogData[i].processed);	
		s3.push(backLogData[i].totalReceived);
		ticks.push(backLogData[i].backlogDate);
		}
	var plot = $.jqplot('backLogChart', [pending, s2, s3], {
            seriesColors:['#e64d00', '#66ff66', '#80b3ff'],
			seriesDefaults: {
                renderer:$.jqplot.BarRenderer,
                pointLabels: { show: true, location: 'n', edgeTolerance: -15 }
            },
            axes: {
				yaxis: {min:0},
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
					ticks: ticks
                }
            },    
			legend: {
                show: true,
                location: 'ne',
				labels: backLogChartLabels,
                placement: 'inside'
            },   
			grid: {
            backgroundColor: '#262628'
        }
		
        });
				
	plot.replot(true,true); 
}

function plotImageReviewStatusBarChart(data){
	console.log('plotImageReviewStatusBarChart');
	var s1 = [];
    var s2 = [];
	var s3 = [];
	var s4 = [];
	var s5 = [];
	var s6 = [];
    var ticks = [];
	var total = 0;
	
	for (var i =0; i<data.length; i++) {	
	    total = 0;
		s1.push(data[i].vsrCount);
		s2.push(data[i].fReview);	
		s3.push(data[i].sReview);
		s4.push(data[i].codeOfRecomended);
		s5.push(data[i].supervisorReview);
		s6.push(data[i].totalCount);
		ticks.push(data[i].reviewDate);
	}
	var plot = $.jqplot('imgReviewStatusChart', [s1, s2, s3, s4, s5, s6], {
            seriesColors:['#2c52aa', '#ce5a25', '#47F3E8', '#edef5d', '#DB5BED', '#69c468'],
			seriesDefaults: {
                renderer:$.jqplot.BarRenderer,
                pointLabels: { show: true, location: 'n', edgeTolerance: -15 }
            },
            axes: {
				yaxis: {min:0},
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
					ticks: ticks
                }
            },    
			legend: {
                show: true,
                location: 'ne',
				labels: imgReviewStatusPlotLabels,
                placement: 'outside'
            },   
			grid: {
            backgroundColor: '#262628'
        }
		
        });
				
	plot.replot(true,true); 
	
}

function plotImageReviewCompletedStatusBarChart(data){
	console.log('plotImageReviewCompletedStatusBarChart');
	var s1 = [];
    var s2 = [];
	var s3 = [];
        var s4 = [];
    var ticks = [];
	//var completed = 0;
	var sum = 0;
	
	var summary = [];
	for (var i =0; i<data.length; i++) {	
		//completed = 0;
		sum = 0;
		s1.push(data[i].autoComplete);
		//completed = data[i].singleMir + data[i].doubleMir;
		s2.push(data[i].completed);	
		s3.push(data[i].codeOff);
                s4.push(data[i].trComplete);
		sum = data[i].autoComplete + data[i].completed + data[i].codeOff + data[i].trComplete;
		summary.push(sum);
		ticks.push(data[i].reviewDate);
	}
	var plot = $.jqplot('imgReviewCompletedStatusChart', [s4, s3, s2, s1, summary], {
            seriesColors:['#2c52aa', '#eab839','#ef843c', '#e24d42', '#69c468'],
			seriesDefaults: {
                renderer:$.jqplot.BarRenderer,
                pointLabels: { show: true,   edgeTolerance: -50}
            },

			
            axes: {
				yaxis: {min:0, },
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
					ticks: ticks
                }
            },    
			legend: {
                show: true,
                location: 'ne',
				labels: imgReviewCompletedStatusBarChartLabels,
                placement: 'outside'
            },   
			grid: {
            backgroundColor: '#262628'
        }
		
        });
				
	plot.replot(true,true); 
}

function plotAutoVSManualImageBarChart(data){
	var s1 = [];
    var s2 = [];
	var s3 = [];
    var ticks = [];
	var completed = 0;
	var sum = 0;

	var summary = [];
	for (var i =0; i<data.length; i++) {	
		sum = 0;
		s1.push(data[i].autoComplete);
		sum = data[i].autoComplete + data[i].singleMir + data[i].doubleMir;
		s2.push(data[i].singleMir);	
		s3.push(data[i].doubleMir);
		summary.push(sum);
		ticks.push(data[i].reviewDate);
	}
	var plot = $.jqplot('autoImageVSmanualRewiew', [s3, s2, s1, summary], {
            seriesColors:['#eab839','#ef843c', '#e24d42', '#69c468'],
			seriesDefaults: {
                renderer:$.jqplot.BarRenderer,
                pointLabels: { show: true, location: 'n', edgeTolerance: -50}
            },

            axes: {
				yaxis: {min:0, pad: -250,},
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
					ticks: ticks
                }
            },    
			legend: {
                show: true,
                location: 'ne',
				labels: autoImageVSmanualRewiewLabels,
                placement: 'outside'
            },   
			grid: {
            backgroundColor: '#262628'
        }
		
        });
				
	plot.replot(true,true); 
}


