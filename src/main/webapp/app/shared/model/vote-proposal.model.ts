import { Moment } from 'moment';

export interface IVoteProposal {
  id?: number;
  creationDate?: Moment;
  votePoints?: number;
  proposalId?: number;
  profileId?: number;
}

export class VoteProposal implements IVoteProposal {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public votePoints?: number,
    public proposalId?: number,
    public profileId?: number
  ) {}
}
