/*
 * Generated by Eclipse Dirigible based on model and template.
 *
 * Do not modify the content as it may be re-generated again.
 */
exports.getTemplate = function() {
	return {
		"name": "Websocket Service",
		"description": "Websocket service with a Javascript handler",
		"sources": [
		{
			"location": "/template-websocket/websocket.template", 
			"action": "generate",
			"rename": "{{fileName}}.websocket"
		},
		{
			"location": "/template-websocket/service-handler.js.template", 
			"action": "generate",
			"rename": "{{fileName}}-service-handler.js"
		},
		{
			"location": "/template-websocket/client-handler.js.template", 
			"action": "generate",
			"rename": "{{fileName}}-client-handler.js"
		},
		{
			"location": "/template-websocket/client.js.template", 
			"action": "generate",
			"rename": "{{fileName}}-client.js"
		}],
		"parameters": []
	};
};
