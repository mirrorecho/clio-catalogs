

(

ClioLibrary.catalog ([\func, \ringer], {

	~overtone = { arg kwargs;
		var
		overtones = kwargs[\overtones] ? [1, 2, 3, 4, 5, 6, 7, 8],
		overtoneAmps = kwargs[\overtoneAmps] ? [0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		ringTimes = kwargs[\ringTimes] ? [0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1];

		var
		ringAmpScale = \ringAmpScale.kr( kwargs[\ringAmpScale] ? 1/(overtoneAmps.sum) ),
		ringMix = \ringMix.kr( kwargs[\ringMix] ? 1.0 );

		var specsArray = [
			overtones * kwargs[\freq],
			overtoneAmps,
			ringTimes
		];

		var ringSig = Klank.ar(`specsArray, kwargs[\sig]) * ringAmpScale * AmpComp.kr( kwargs[\freq], 20, 0.2);

		kwargs[\sig] = (ringSig * ringMix) + (kwargs[\sig] * (1-ringMix));

	};



	~ringer = { arg kwargs;
		var
		ringFreqs = kwargs[\overtones] ? [1, 2, 3, 4, 5, 6, 7, 8],
		ringAmps = kwargs[\overtoneAmps] ? [0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		ringTimes = kwargs[\ringTimes] ? [0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1];

		var
		ringAmpScale = \ringAmpScale.kr( kwargs[\ringAmpScale] ? 1/(overtoneAmps.sum) ),
		ringMix = \ringMix.kr( kwargs[\ringMix] ? 1.0 );

		var specsArray = [
			overtones * kwargs[\freq],
			overtoneAmps,
			ringTimes
		];

		var ringSig = Klank.ar(`specsArray, kwargs[\sig]) * ringAmpScale;

		kwargs[\sig] = (ringSig * ringMix) + (kwargs[\sig] * (1-ringMix));

	};




});



)


{ Formant.ar(XLine.kr(400,1000, 8), 2000, 800, 0.125) }.play


{ Formant.ar(200, XLine.kr(400, 4000, 8), 200, 0.125) }.play

{ Formant.ar(400, 2000, XLine.kr(800, 8000, 8), 0.125) }.play


(

SynthDef(\KSpluck, { arg midiPitch = 69, delayDecay = 0.5;

	var burstEnv, att = 0, dec = 0.0001;

	var signalOut, delayTime;


	delayTime = [midiPitch, midiPitch + 12].midicps.reciprocal;

	burstEnv = EnvGen.ar(Env.perc(att, dec));

	signalOut = WhiteNoise.ar(burstEnv);
	signalOut = BPF.ar(signalOut, midiPitch.midicps);

	signalOut = CombL.ar(signalOut, delayTime, delayTime, delayDecay, add: signalOut);

	DetectSilence.ar(signalOut, doneAction:2);

	Out.ar(0, signalOut)

	}

).store;

)



(

//Then run this playback task

r = Task({

	{Synth(\KSpluck,

		[

			// \midiPitch, rrand(30, 90), //Choose a pitch

		\delayDecay, rrand(0.5, 3.0) //Choose duration

		]);

		//Choose a wait time before next event

		[0.125, 0.125, 0.25].choose.wait;

	}.loop;

}).play

)

