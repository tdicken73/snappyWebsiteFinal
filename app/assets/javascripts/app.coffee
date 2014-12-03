utils = window.angular.module('utils' , [])

utils.filter('timer' ,['time', (time) ->
  (input) ->
    if input
      seconds=toSeconds(input)
      minutes=toMinutes(input)
      hours=toHours(input)
      "#{hours}:#{minutes}:#{seconds}"
    else
      "Press Start"
]).service('time', ->
    @toHours = (timeMillis) -> addZero((timeMillis/(1000*60*60)))
    @toMinutes = (timeMillis) -> addZero((timeMillis/(1000*60))%60)
    @toSeconds = (timeMillis) -> addZero((timeMillis/1000)%60)
    @toTime = (hours,minutes,seconds) -> ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000
    @addZero = (value) ->
      value = Math.floor(value)
      if(value < 10)
        "0#{value}"
      else
        value

).controller('TimerController', ($scope, $http) ->
  $scope.node1 = []
  node2 = []
  node3 = []
  node4 = []
  node5 = []
  node6 = []
  node7 = []
  node8 = []
  node9 = []
  node10 = []
  startWS = ->
    wsUrl = jsRoutes.controllers.AppController.indexWS().webSocketURL()
    $scope.socket = new WebSocket(wsUrl)

    $scope.socket.onmessage = (msg) ->
      $scope.$apply( ->
  
 
        $scope.node = JSON.parse(msg.data).node
        $scope.data = JSON.parse(msg.data).data
        
        console.log "received  node: #{$scope.node}"
        console.log "received  data: #{$scope.data}"
        
        if($scope.node == "node1")
        	node1.push $scope.data
        	console.log "node1 list is at: #{node1}"
        if($scope.node == "node2")
        	node2.push $scope.data
        if($scope.node == "node3")
        	node3.push $scope.data
        if($scope.node == "node4")
        	node4.push $scope.data
        if($scope.node == "node5")
        	node5.push $scope.data
        if($scope.node == "node6")
        	node6.push $scope.data
        if($scope.node == "node7")
        	node7.push $scope.data
        if($scope.node == "node8")
        	node8.push $scope.data
        if($scope.node == "node9")
        	node9.push $scope.data
        if($scope.node == "node10")
        	node10.push $scope.data
        
      )
	

   
  $scope.start = ->
    $http.get(jsRoutes.controllers.AppController.start().url).success( -> )

  $scope.stop = ->
    $http.get(jsRoutes.controllers.AppController.stop().url).success( -> )

  startWS()

) 

window.angular.module('app' , ['utils'])

addZero = (value) ->
  value = Math.floor(value)
  if(value < 10)
    "0#{value}"
  else
    value

toHours = (time) ->
  addZero((time/(1000*60*60)))
toMinutes = (time) ->
  addZero((time/(1000*60))%60)
toSeconds = (time) ->
  addZero((time/1000)%60)