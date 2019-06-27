import { Moment } from 'moment';
import { IProposal } from 'app/shared/model/proposal.model';
import { IVoteProposal } from 'app/shared/model/vote-proposal.model';

export interface IProposalUser {
  id?: number;
  creationDate?: Moment;
  assignedVotesPoints?: number;
  userId?: number;
  proposals?: IProposal[];
  voteProposals?: IVoteProposal[];
}

export class ProposalUser implements IProposalUser {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public assignedVotesPoints?: number,
    public userId?: number,
    public proposals?: IProposal[],
    public voteProposals?: IVoteProposal[]
  ) {}
}
