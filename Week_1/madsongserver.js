// An extremely simple server for use in MAD topic 8.
// Loads songs from file, so does not need a database.
// Use if you are NOT doing COM518. If you are doing COM518, use your server
// from that module instead.
//
// You need to install dependencies:
//
// npm install express
//
// and then run the server:
//
// node madsongserver.js
//
// To access from your app on your virtual device:
// 
// http://10.0.2.2:3000/artist/<artist name>
//
// or
//
// http://10.0.2.2:3000/song/create (with POST data)

const fs = require('fs').promises;
fs.readFile('./songs.json').then ( contents => {
    const data = JSON.parse(contents);
    const express = require('express');
    const app = express();
    app.use(express.urlencoded({extended: false}));

    app.get('/artist/:artist', (req, res) => {
        const results = data.filter ( song => song.artist == req.params.artist );
        res.json(results);
    });

    app.post('/song/create', (req, res) => {
		console.log(`/song/create received this data: ${JSON.stringify(req.body)}`);
        data.push(Object.assign({}, req.body));
        res.json({success: 1});
    });

    app.listen(3000);
}).catch(e => { console.log(e); });
        
