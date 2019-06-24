(

// the functions here
// adds percussive elements to a SynthDef, such as noisy initial strike or bass-drum like tones

ClioLibrary.catalog ([\func, \sampler], {

	// rand tones from sine waves
	// at low freqs with perc env this works as a bass drum, at mid/high freqs it's a funny alien sounding thingy
	~define = { arg kwargs;

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


		kwargs[\getSample] = {arg freq;
			var whichSample = 0;

			cutoverFreqs[..cutoverFreqs.size-2].do {|cutoverFreq, i|
				whichSample = whichSample + ((i+1) * (freq >= cutoverFreq) * (freq < cutoverFreqs[i+1]));
			};

			if (cutoverFreqs.size > 0,
				{ whichSample = whichSample + ((cutoverFreqs.size) * (freq >= cutoverFreqs[cutoverFreqs.size-1]));}
			);

			Select.kr(whichSample, buffersAndFreqs);
		};

	};

	~play = { arg kwargs;

		var numChannels = kwargs[\def_numChannels] ? 2;
		var start = \start.ir( kwargs[\start] ? 0);

		var bufnum, bufferFreq, rate, sig;

		#bufnum, bufferFreq = kwargs[\getSample].value(kwargs[\freq]);
		rate = kwargs[\freq] / bufferFreq;

		sig = PlayBuf.ar(
			numChannels: numChannels,
			bufnum:bufnum,
			rate:BufRateScale.kr(bufnum)*rate,
			startPos:BufSampleRate.ir(bufnum) * start,
			doneAction:2,
		);

		kwargs[\sig] = kwargs[\sig] + sig;

	};

});

)


