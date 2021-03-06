﻿// *************************************************************** IO

// http://codingforums.com/showthread.php?t=143412

// LA MOD String Version. 
// A tiny ajax library by DanDavis

// Revised 20101006
// http://www.quirksmode.org/js/xmlhttp.html

var XMLHttpFactories = [
	function ( ) 
	  {
	  return ( new XMLHttpRequest ( ) );
	  },
	function ( ) 
	  {
	  return ( new ActiveXObject ( "Msxml2.XMLHTTP" ) );
	  },
	function ( ) 
	  {
	  return ( new ActiveXObject ( "Msxml3.XMLHTTP" ) );
	  },
	function ( ) 
	  {
	  return ( new ActiveXObject ( "Microsoft.XMLHTTP" ) );
	  }
  ];

// ********************************************** createXMLHTTPObject

function createXMLHTTPObject ( ) 
  {
  var xmlhttp = false;

  for ( var i = 0; ( i < XMLHttpFactories.length ); i++ ) 
    {
    try
      {
      xmlhttp = XMLHttpFactories [ i ] ( );
      }

    catch ( e ) 
      {
      continue;
      }

    break;
    }

  return ( xmlhttp );
  }

// **************************************************** read_contents

function read_contents ( url ) 
  {
  var request = createXMLHTTPObject ( );

  request.open ( 'GET', url);
  request.setRequestHeader ( 'Content-Type', 'text/html' );
  request.onload = handler;
  request.send ();

  return ( request.responseText );
  }

function handler() {
	  if(this.status == 200 &&
	    this.responseXML != null &&
	    this.responseXML.getElementById('test').textContent) {
	    // success!
	    processData(this.responseXML.getElementById('test').textContent);
	  } else {
	  }
	}
