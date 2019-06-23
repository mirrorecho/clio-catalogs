(

ClioLibrary.catalog ({

	~play = ClioSamplerFactory(*[
		channels: 2,
		out:Clio.busses[\master],

		{ arg name, kwargs;


			SynthDef(name, {
				arg amp=1.0, start=0, freq=440, out = kwargs[\out];
				var buffer, bufferFreq, rate, sig;

				#buffer, bufferFreq = kwargs[\getSample].value(freq);
				rate = freq / bufferFreq;

				sig = PlayBuf.ar(
					numChannels: kwargs[\channels],
					bufnum:buffer,
					rate:BufRateScale.kr(buffer)*rate,
					startPos:BufSampleRate.ir(buffer) * start,
					doneAction:2,
				);

				sig = sig * amp;

				Out.ar(out, sig);

			});
		},
	]);



}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)


