Klang

(
SynthDef(\spacey, { arg freq=440, amp=0.1, gate=1, out=0;
	var sig1, sig2, env1, env2;
	// // TO DO... this part could be added separately with a Klang-based
	// // synth def function based on the harmonic series
	// sig1 = SinOsc.ar(freq:freq, mul:amp/2);
	// sig1 = sig1 + SinOsc.ar(freq:freq*2, mul:amp/3);
	// env1 = EnvGen.kr(Env.adsr(0.02, 0.1, 0.4, 0.9), gate:gate);
	// sig1 = sig1 * env1;

	sig2 = Saw.ar(freq:freq, mul:amp/6);
	sig2 = RLPF.ar(sig2, LFNoise1.kr(8!2).range(freq*2, 9900), 0.2);
	env2 = EnvGen.kr(Env.adsr(0.8, 0.2, 0.4, 3), gate:gate, doneAction:2);

	sig2 = sig2 * env2;
	Out.ar(out, sig2);

	// sig1 = sig1!2 + sig2;
	// Out.ar(out, sig1!2);

}).add;
)
x = Synth(\spacey);
x.set(\gate, 0);


	/*	// A Piano note SynthDef as a combination of its harmonics
	SynthDef("rain.piano",{ arg freq = 261.63, amp = 1, gate = 1, out=~ss.bus.master;
	var ampls = [3.7, 5.4, 1.2, 1.1, 0.95, 0.6, 0.5, 0.65, 0, 0.1, 0.2];
	var freqs = Array.fill(ampls.size, { |i| freq * (i + 1) });
	var waves = Array.fill(ampls.size, { |i| SinOsc.ar(freqs.at(i),mul: ampls.at(i))});
	var mixedwaves = Mix.ar(waves).range(amp * -1, amp) * 0.1;
	var env = Env.perc(0.09, 2,curve: -12);
	var final = mixedwaves * EnvGen.ar(env, gate, doneAction: 2);
	Out.ar(out, final!2);
	}).add;*/

(
SynthDef(\pop, {
	arg freq=440, gate=1, amp=1.0, slideTime = 1.0, out= ~ss.bus.master;
	var sig, sig2, env;
	freq = Lag.kr(freq, slideTime);
	sig = Resonz.ar(WhiteNoise.ar(1.98!2), freq, 0.04, 22) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 22);
	sig = sig * amp * 8;
	sig2 = Splay.ar(sig, spread:0.9);
	sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.4);
	env = EnvGen.kr(Env.perc, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(out, sig2);
}).add;
)

(
SynthDef(\ringy, {arg freq=440;
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	Out.ar(0,
        Pan2.ar(tone*0.6, 0)
    )
}).add;
)
Synth(\ringy, [freq:880]);

(
SynthDef(\getGastro, { arg out = ~ss.bus.master, bufnum = ~ss.buf.gastro.knob9, inputBus = 0;
	var sig = SoundIn.ar(inputBus), freq, hasFreq;
	RecordBuf.ar(sig, bufnum, doneAction: Done.freeSelf, loop: 0);
	# freq, hasFreq = Pitch.kr(sig, ampThreshold: 0.02, median: 7);
	Out.kr(~ss.bus.pollPitch, freq);
}).add;
)

(
SynthDef(\siny, {arg out=0, freq=440, fadeIn=2, sustain=4, fadeOut=2, mul=0.6;
	var outArray = [SinOsc.ar(freq, 0, mul/4), SinOsc.ar(freq * 1.001, 0, mul/4)];
	Out.ar(out,
		outArray
		* EnvGen.ar(Env.linen(fadeIn, sustain, fadeOut),doneAction:2)
		* mul
		,0.0);
}).add;
)

(
SynthDef(\noiseSwell, {arg out=0, fadeIn=2, fadeOut=1, mulPeak=0.6, mulAfter=0.02;
	// note... would be cool to try band-limiting this?
	var outArray1 = [
		WhiteNoise.ar(1/4),
		WhiteNoise.ar(1/4)
	];
	var outArray2 = [
		PinkNoise.ar(1/6),
		PinkNoise.ar(1/6)
	];
	Out.ar(out,
		outArray1
		* EnvGen.ar(Env([0.00000001, mulPeak, mulAfter, 0.00000001], [fadeIn, 0.01, fadeOut], \exp), doneAction:2)
		+ outArray2
		* EnvGen.ar(Env([0, mulPeak/2, mulAfter, 0.00000001], [fadeIn, 0.01, fadeOut], \lin), doneAction:2)
		,0.0);
}).add;

)
Synth(\noiseSwell);

(
SynthDef(\weirdo, {
	arg noiseHz=12, hiFreq=440, loFreq=880, weirdCount=4, gate=1;
	var amp, freq, sig, sig2, env, weirdCount2=8;
	freq = LFNoise2.kr(noiseHz!weirdCount2).exprange(loFreq, hiFreq);
	amp = LFNoise1.kr(noiseHz!weirdCount2).exprange(0.01, 0.6);
	amp = amp / weirdCount;
	sig = SinOsc.ar(freq) * amp;
	sig2 = Splay.ar(sig, spread:0.6);
	// sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.1);
	env = EnvGen.kr(Env.asr, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(~ss.bus.master, sig2);
}).add;
)


(
SynthDef(\bloo, {
	arg freq = 440;
	var temp, sum=0, numBloo=16;
	numBloo.do{
		arg count;
		temp = SinOsc.ar(
			freq
			* LFNoise2.kr({Rand(0.2, 4)}!2).exprange(0.99, 1.01)
			* (count + 1),
			mul: 1 / ((count+1)/2)
		);
		temp = temp * LFNoise2.kr({Rand(0.5, 4)}!2).exprange(0.01, 1);
		sum = sum + temp;
	};
	sum = (sum / numBloo) * 0.8;
	sum = FreeVerb2.ar(sum[0], sum[1]);
	Out.ar(~ss.bus.master, sum);
}).add;

)

(
// same as above but freq and mul have lag
SynthDef("ss.bloo", {
	var temp, sum=0, numBloo=12;

	var mul = Lag.kr(In.kr(~controlBusBack), 2);
	// var mul = 1;

	var freq = Lag.kr(90*(1.5**In.kr(~controlBusBackCount)), 3);

	numBloo.do{
		arg count;
		temp = SinOsc.ar(
			freq
			* LFNoise2.kr({Rand(0.2, 4)}!2).exprange(0.9, 1.1)
			* (count + 1),
			mul: 1 / ((count+1)/2)
		);
		temp = temp * LFNoise2.kr({Rand(0.5, 4)}!2).exprange(0.01, 1);
		sum = sum + temp;
	};
	sum = (sum / numBloo) * 0.8;
	Out.ar(~ss.bus.master, sum*mul);
}).add;
)

(

SynthDef(\weirdo, {
	arg noiseHz=12, hiFreq=440, loFreq=880, weirdCount=4, gate=1;
	var amp, freq, sig, sig2, env, weirdCount2=8;
	freq = LFNoise2.kr(noiseHz!weirdCount2).exprange(loFreq, hiFreq);
	amp = LFNoise1.kr(noiseHz!weirdCount2).exprange(0.01, 0.6);
	amp = amp / weirdCount;
	sig = SinOsc.ar(freq) * amp;
	sig2 = Splay.ar(sig, spread:0.6);
	sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.1);
	env = EnvGen.kr(Env.asr, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(0, sig2);
}).add;

)


(
SynthDef(\cooleo, {
	arg freq=440;
	var temp, env, sum=0, numCools=44;
	numCools.do{
		temp = VarSaw.ar(
			freq * {Rand(0.99, 1.02)}!2,
			iphase: {Rand(0.0, 1.0)}!2,
			width: {ExpRand(0.01, 1)}!2,
			mul: 1/numCools);
		sum = sum + temp;
	};
	env = EnvGen.kr(Env.perc, doneAction:2);
	Out.ar(0, sum * env);
}).add;

SynthDef(\bloo, {
	arg freq = 440;
	var temp, sum=0, numBloo=16;
	numBloo.do{
		arg count;
		temp = SinOsc.ar(
			freq
			* LFNoise2.kr({Rand(0.2, 4)}!2).exprange(0.99, 1.01)
			* (count + 1),
			mul: 1 / ((count+1)/2)
		);
		temp = temp * LFNoise2.kr({Rand(0.5, 4)}!2).exprange(0.01, 1);
		sum = sum + temp;
	};
	sum = (sum / numBloo) * 0.8;
	sum = FreeVerb2.ar(sum[0], sum[1]);
	Out.ar(0, sum);
}).add;


)

// NEW SYNTHS FOR CLUB NIGHT
(
SynthDef("ss.saw", {
	CombC.ar(Saw.ar(LFPulse.ar(1!2)*f+f+(LFPulse.ar(y/9)*f+g)+(LFPulse.ar(y)*f)+(LFPulse.ar(1/y)*g)+(LFPulse.ar(y*2)*g))*(6-y)/22)}
}
).add;
)

(
// see more pops below!
SynthDef("ss.pops." ++ respondTo, {
	// var times=4, respondTo=1;
	// var mul = Lag.kr(In.kr(~controlBusForward8), 2);
	var forwardCount = In.kr(~controlBusCounter) * In.kr(~controlBusForward);
	// var forwardCount = 1;
	var trigger = BinaryOpUGen('==', respondTo, forwardCount);
	var mul = Lag.kr(trigger, 2);
	var freq, r;
	var sig = Splay.ar(
		{|i|
			r=i+1/8;
			freq = r/(i+2*3);
			Formlet.ar(
				Impulse.ar(r),
				LFSaw.ar(freq,1)+2*3**4,
				0.01,1/r/2)
		}!times, 0.4
	)*mul;
	Out.ar(~ss.bus.master, sig);
}
).add;
)

(
SynthDef("ss.pops8", {
	var times = 4;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward8), 2);
	Out.ar(~ss.bus.master,
		Splay.ar(
			{|i|
				r=i+1/8;
				freq = r/(i+2*3);
				Formlet.ar(
					Impulse.ar(r),
					LFSaw.ar(freq,1)+2*3**4,
					0.01,1/r/2)
			}!times, 0.4
		)*mul;
	);
}
).add;

SynthDef("ss.pops12", {
	var times = 12;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward12), 2);
	Out.ar(~ss.bus.master,
		Splay.ar(
			{|i|
				r=i+1/8;
				freq = r/(i+2*3);
				Formlet.ar(
					Impulse.ar(r),
					LFSaw.ar(freq,1)+2*3**4,
					0.01,1/r/2)
			}!times, 0.4
		)*mul;
	);
}
).add;

SynthDef("ss.pops16", {
	var times = 24;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward16), 4);
	Out.ar(~ss.bus.master,
		Splay.ar({|i|
			Ringz.ar(
				Decay.ar(
					Impulse.ar(r=i+1/4),1/r,
					Crackle.ar/6),
				LFSaw.ar(f=r/(i+2*3),1)+2*3**4,f)}!times,0.4)*mul;
	);
}
).add;

SynthDef("ss.bloo", {
	var temp, sum=0, numBloo=12;

	var mul = Lag.kr(In.kr(~controlBusBack), 2);
	var freq = Lag.kr(90*(1.5**In.kr(~controlBusBackCount)), 3);

	numBloo.do{
		arg count;
		temp = SinOsc.ar(
			freq
			* LFNoise2.kr({Rand(0.2, 4)}!2).exprange(0.9, 1.1)
			* (count + 1),
			mul: 1 / ((count+1)/2)
		);
		temp = temp * LFNoise2.kr({Rand(0.5, 4)}!2).exprange(0.01, 1);
		sum = sum + temp;
	};
	sum = (sum / numBloo) * 0.8;
	Out.ar(~ss.bus.master, sum*mul);
}).add;

SynthDef("ss.ringy", {
	var counter = Lag.kr(In.kr(~controlBusCounter), 0.1);
	var counterControl = Lag.kr(In.kr(~controlBusForward16) + In.kr(~controlBusForward8), 0.1);

	var freq=45*( (9.5/8) **counter);
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	var ghosts = Resonz.ar(Crackle.ar(1.98!2), freq, 0.04, 12) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 6) +
	Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 4) +
	Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 2) +
	Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 1);

	Out.ar(~ss.bus.master,
		(Pan2.ar(tone*0.1, 0) + ghosts*0.3 ) * 0.2 * counterControl
    )
}).add;

// similar to above ...
SynthDef("ss.ringy", {
	var counter = Lag.kr(In.kr(~controlBusCounter), 0.1);
	var forwardCount = In.kr(~controlBusCounter);
	var isForward = In.kr(~controlBusForward);
	var mulTrigger = (forwardCount <= 24) * isForward;


	var freq=45*( (9.5/8) **counter);
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	var ghosts = Resonz.ar(Crackle.ar(1.98!2), freq, 0.04, 12) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 6) +
	Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 4) +
	Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 2) +
	Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 1);

	Out.ar(~ss.bus.master,
		(Pan2.ar(tone*0.1, 0) + ghosts*0.3 ) * 0.2 * mulTrigger;
		)
	}).add;

// the take-aways for this one are:
// - the delayN to create drone
// - crackle at the end
SynthDef("ss.mower",{ arg out=0, rate=1, fadeIn=1, sustain=1, fadeOut=1;

	var fileName = "/Users/rwest/Downloads/Lawn mower short clip 2.wav";
	var mul = Lag.kr(In.kr(~controlBusMower), 12);

	var bufSampleRate = SoundFile(fileName).sampleRate;

	var startOn = 0;
	var endOn = 3;
	var length = endOn - startOn;

	// NOTE... better off to use cue (or some other method for SoundFile instead of re-reading the sound file)
	var soundBuf = Buffer.new;

	soundBuf.allocRead(fileName, bufSampleRate*startOn, bufSampleRate*length);
		z = PlayBuf.ar(
			numChannels:2,
			bufnum:soundBuf.bufnum,
			rate:BufRateScale.kr(soundBuf.bufnum)*rate,
		loop:1) * EnvGen.ar(Env.circle([0,1,0], [length/(2*rate), length/(2*rate), 0]));
	Out.ar(~ss.bus.master,
		// z
		// dividing by rate is important to adjust circle to any possible rate...
		DelayN.ar(z, length/(2*rate), length/(2*rate), 1, z)
		 + Crackle.ar(1.98, 0.1)
		// * EnvGen.ar(Env.linen(fadeIn, sustain, fadeOut),doneAction:2)
		* mul * 0.6
		,0.0);
}).add;


)
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// Code from others:
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------

// from here: "http://doc.sccode.org/Tutorials/Streams-Patterns-Events1.html"

(
    s = Server.local;
    SynthDef(\help_SPE1, { arg i_out=0, freq;
        var out;
        out = RLPF.ar(
            LFSaw.ar( freq, mul: EnvGen.kr( Env.perc, levelScale: 0.3, doneAction: 2 )),
            LFNoise1.kr(2, 36, 110).midicps,
            0.1
        );
        //out = [out, DelayN.ar(out, 0.04, 0.04) ];
        4.do({ out = AllpassL.ar(out, 0.5, [0.05.rand, 0.05.rand], 4) });
        Out.ar( i_out, out );
    }).send(s);
)

// ----------------------------------------------------------------------------------------------------------