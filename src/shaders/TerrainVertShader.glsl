#version 330 core 

in vec3 position;
in vec3 normals;
uniform vec3 lightPosition;
in vec2 textCoords;
out vec2 passTextCoords;
out vec3 faceNormal;
out vec3 toCamera;
out vec3 toLight;
out vec4 shadowCoords;
out float visibility;
out float depth;

const float fogDensity = 0.02;
const float fogGradient = 1.5;

uniform mat4 shadowMapP;
uniform mat4 shadowMapV;
uniform mat4 p;
uniform mat4 v;
uniform mat4 t;

void main(void) {
	
	
	vec4 worldPosition = t * vec4(position, 1.0);
	shadowCoords = shadowMapP * shadowMapV * worldPosition * 0.5 + 0.5;
	vec4 posRelativeToCam = v * worldPosition;
	gl_Position = p * posRelativeToCam;
	passTextCoords = textCoords * 40.0;
	
	faceNormal = vec3(t * vec4(normals, 0.0));

	toLight = lightPosition - worldPosition.xyz;
	toCamera = (inverse(v) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	float distance = length(posRelativeToCam.xyz);
	//visibility = exp(-pow((distance*fogDensity), fogGradient));
	visibility = 1;
}