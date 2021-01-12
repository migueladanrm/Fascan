import { NextFunction, Request, Response } from "express";

export function authenticateRequest(
  req: Request,
  res: Response,
  next: NextFunction
) {
  try {
    const serverAuthKey = process.env.SERVER_AUTH_KEY;
    const userAuthKey = req.headers.authorization;

    if (
      serverAuthKey != undefined &&
      userAuthKey != undefined &&
      serverAuthKey == userAuthKey
    ) {
      next();
    } else {
      res.status(401).json({ error: "Invalid authentication." });
    }
  } catch {
    return res.status(401).send({ error: "Error on authentication." });
  }
}
