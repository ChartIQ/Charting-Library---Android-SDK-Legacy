function nativeQuoteFeed(parameters, cb) {
	if (parameters.func === "pullInitialData") {
		var id=CIQ.uniqueID();
		quoteFeedCallbacks[id] = cb;
		QuoteFeed.pullInitialData(parameters.symbol, parameters.period, parameters.timeUnit, parameters.start.toISOString(), parameters.end.toISOString(), parameters, id);
	}
	
	if (parameters.func === "pullUpdate") {
		var id=CIQ.uniqueID();
		quoteFeedCallbacks[id] = cb;
		QuoteFeed.pullUpdate(parameters.symbol, parameters.period, parameters.timeUnit, parameters.start.toISOString(), parameters, id);
	}
	
	if (parameters.func === "pullPagination") {
		var id=CIQ.uniqueID();
		quoteFeedCallbacks[id] = cb;
		QuoteFeed.pullPagination(parameters.symbol, parameters.period, parameters.timeUnit, parameters.start.toISOString(), parameters.end.toISOString(), parameters, id); 
	}
}

function parseData(data, callbackId) {
	var feeddata=JSON.parse(data);
	var newQuotes=[];
	
	for(var i=0;i<feeddata.length;i++){
		newQuotes[i]={};
		newQuotes[i].DT=new Date(feeddata[i].DT);
		newQuotes[i].Open=feeddata[i].Open;
		newQuotes[i].High=feeddata[i].High;
		newQuotes[i].Low=feeddata[i].Low;
		newQuotes[i].Close=feeddata[i].Close;
		newQuotes[i].Volume=feeddata[i].Volume;
	}
	
	var quoteFeedCb = quoteFeedCallbacks[callbackId];
	quoteFeedCb({quotes:newQuotes, moreAvailable:true, attribution:{source:"simulator", exchange:"RANDOM"}});
}

function callNewChartWithPeriodicity(symbol) {
	stxx.newChart(symbol, null, null, null,
			{periodicity:{period:stxx.layout.periodicity,interval:stxx.layout.interval}});
}


function attachQuoteFeed(refreshInterval) {
	if(stxx) {
		stxx.attachEngineQuoteFeed(quoteFeedNativeBridge,{refreshInterval: refreshInterval});
	}
}

function getSymbol() {
	return stxx.chart.symbol;
}

function enableCrosshairs(active) {
	stxx.layout.crosshair = active;
}

function getCurrentVectorParameters() {
	return stxx.currentVectorParameters;
}

function setCurrentVectorParameters(parameter, value) {
	stxx.currentVectorParameters[parameter] = value;
}

function removeStudy(studyName) {
	CIQ.Studies.removeStudy(stxx, stxx.layout.studies[studyName]);
}

function getStudyList() {
	var result = []; 
	
	for(var key in CIQ.Studies.studyLibrary) {
		CIQ.Studies.studyLibrary[key].shortName = key;
		result.push(CIQ.Studies.studyLibrary[key]);
	}
	
	return result;
}

function getActiveStudies() {
	var result = [];
	
	for(var key in stxx.layout.studies) {
		stxx.layout.studies[key].shortName = key;
		result.push(stxx.layout.studies[key]);
	}
	
	return result;
}

function getStudyParameters(studyName, isInput) {
	var helper = new STX.Studies.DialogHelper({sd:stxx.layout.studies[studyName], stx:stxx});
	var parameters = helper.outputs;
	
	if(isInput) {
		parameters = helper.inputs;
	}
	
	return parameters;
}

function setStudyParameter(studyName, key, value, isInput) {
	var helper = new CIQ.Studies.DialogHelper({sd:stxx.layout.studies[studyName], stx:stxx});
	
	if(isInput) {
		helper.updateStudy({inputs:{
			key: value
		}, outputs:{}});
	} else {
		helper.updateStudy({inputs:{}, outputs:{
			key: value
		}});
	}
}
