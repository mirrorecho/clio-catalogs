(

// the functions here
// adds percussive elements to a SynthDef, such as noisy initial strike or bass-drum like tones

ClioLibrary.catalog ([\func, \sampler], {

	~getSample = ClioSynthFunc({ arg kwargs;

		var buffersAndFreqs = kwargs[\keysAndFreqs].collect { arg keyAndFreq, i;
			[
				kwargs[\soundLibrary].at(*keyAndFreq[0]).buffer, // the buffer
				keyAndFreq[1], // the frequency of the sound for this buffer
			];
		};

		var cutoverFreqs = buffersAndFreqs[..buffersAndFreqs.size-2].collect {arg bufferAndFreq, i;
			var
			freq = bufferAndFreq[1],
			nextFreq = buffersAndFreqs[i+1][1];

			freq * ((nextFreq/freq)**0.5);
		};


		var freq = kwargs[\synth][\freq];

		var whichSample = 0;

		cutoverFreqs[..cutoverFreqs.size-2].do {|cutoverFreq, i|
			whichSample = whichSample + ((i+1) * (freq >= cutoverFreq) * (freq < cutoverFreqs[i+1]));
		};

		if (cutoverFreqs.size > 0,
			{ whichSample = whichSample + ((cutoverFreqs.size) * (freq >= cutoverFreqs[cutoverFreqs.size-1]));}
		);

		Select.kr(whichSample, buffersAndFreqs);

	}, *[
		keysAndFreqs:nil,
		buffersAndFreqs:nil,
	]);


	~play = ClioSynthFunc({ arg kwargs;
		var bufnum, bufferFreq, rate;

		#bufnum, bufferFreq = kwargs[\sample];
		rate = kwargs[\synth][\freq] / bufferFreq;

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + PlayBuf.ar(
			numChannels: kwargs[\numChannels],
			bufnum:bufnum,
			rate:BufRateScale.kr(bufnum)*rate,
			startPos:BufSampleRate.ir(bufnum) * kwargs[\start],
			doneAction:kwargs[\doneAction],
		);

	}, *[
		sample:[nil,nil],
		doneAction:2,
		numChannels:2,
		start:0,
	]);

	// TO DO... add float


});

)


