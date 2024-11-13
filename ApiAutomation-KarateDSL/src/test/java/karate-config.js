function fn() {

  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'dev';
  }
  var config = {
    apiUrl: 'https://petstore.swagger.io/'
  }

  let sessionId = karate.callSingle('classpath:helpers/Login.feature', config).sessionId
//  karate.configure('headers', {Authorization: 'Token ' + accessToken})

  return config;
}