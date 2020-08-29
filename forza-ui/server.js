const express = require('express');
const app = express();

app.use(express.static(__dirname + '/dist/forza-ui'));

app.get('/*', function (req, res) {
  res.sendFile(__dirname + '/dist/forza-ui/index.html');
});

app.listen(process.env.PORT || 8080);