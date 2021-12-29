#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visiblity;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;


void main(void){
    vec3 lightColour = vec3(242, 247, 244);
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    vec3 attenuation = vec3(15,15,15);
    float attFactor = attenuation.x + attenuation.y + attenuation.z;
    vec3 unitLightVector = normalize(toLightVector);
    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0.0);

    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    totalDiffuse = totalDiffuse + (brightness * lightColour) / attFactor;
    totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour) / attFactor;


    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(modelTexture, pass_textureCoordinates);
    if(textureColor.a < 0.5){
        discard;
    }

    out_Color =  vec4(totalDiffuse,1.0) * texture(modelTexture, pass_textureCoordinates) + vec4(totalSpecular,1.0);
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visiblity);
}